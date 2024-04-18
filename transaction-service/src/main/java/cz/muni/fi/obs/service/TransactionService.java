package cz.muni.fi.obs.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cz.muni.fi.obs.api.CurrencyExchangeRequest;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.http.CurrencyServiceClient;

@Service
public class TransactionService {

	private final TransactionRepository repository;
	private final CurrencyServiceClient client;

	@Autowired
	public TransactionService(TransactionRepository repository, CurrencyServiceClient client) {
		this.repository = repository;
		this.client = client;
	}

	public BigDecimal checkAccountBalance(String accountId) {
		var withdraws = repository.findTransactionsDboByWithdrawsFrom_Id(accountId);
		var deposits = repository.findTransactionsDboByDepositsTo_Id(accountId);

		BigDecimal withdrawSum = withdraws.stream()
				.map(TransactionDbo::getWithdrawAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal depositSum = deposits.stream()
				.map(TransactionDbo::getDepositAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return depositSum.subtract(withdrawSum);
	}

	public Page<TransactionDbo> viewTransactionHistory(String accountId, int pageNumber, int pageSize) {
		PageRequest request = PageRequest.of(pageNumber, pageSize);
		return repository.findTransactionHistory(accountId, request);
	}

	public Optional<TransactionDbo> getTransactionById(String id) {
		return repository.findById(id);
	}

	public void createTransaction(TransactionCreateDto transaction) {
		CurrencyExchangeRequest request = CurrencyExchangeRequest.builder()
				.from(transaction.withdrawsFromAccountNumber())
				.to(transaction.depositsToAccountNumber())
				.amount(transaction.depositAmount())
				.build();

		var conversionRate = client.getCurrencyExchange(request)
				.orElseThrow(() -> new ResourceNotFoundException("Currency exchange rate not found"));

		var transactionDbo = TransactionDbo.builder()
				.id(UUID.randomUUID().toString())
				.withdrawsFrom(AccountDbo.builder().build())
				.note(transaction.note())
				.depositsTo(AccountDbo.builder().build())
				.depositAmount(transaction.depositAmount())
				.withdrawAmount(transaction.withdrawAmount())
				.variableSymbol(transaction.variableSymbol())
				.conversionRate(conversionRate.exchangeRate())
				.build();

		repository.save(transactionDbo);
	}
}
