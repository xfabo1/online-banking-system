package cz.muni.fi.obs.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.muni.fi.obs.api.CurrencyExchangeRequest;
import cz.muni.fi.obs.api.CurrencyExchangeResult;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.http.CurrencyServiceClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionService {

	private final TransactionRepository repository;
	private final CurrencyServiceClient client;

	@Autowired
	public TransactionService(TransactionRepository repository, CurrencyServiceClient client) {
		this.repository = repository;
		this.client = client;
	}

	@Transactional
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

	@Transactional
	public TransactionDbo createTransaction(TransactionCreateDto transaction, AccountDbo withdrawAccount,
			AccountDbo depositAccount) {
		CurrencyExchangeRequest request = CurrencyExchangeRequest.builder()
				.from(transaction.withdrawsFromAccountNumber())
				.to(transaction.depositsToAccountNumber())
				.amount(transaction.depositAmount())
				.build();

		CurrencyExchangeResult conversionRate = callCurrencyClient(request);

		var transactionDbo = TransactionDbo.builder()
				.id(UUID.randomUUID().toString())
				.withdrawsFrom(withdrawAccount)
				.note(transaction.note())
				.depositsTo(depositAccount)
				.depositAmount(transaction.depositAmount())
				.withdrawAmount(transaction.withdrawAmount())
				.variableSymbol(transaction.variableSymbol())
				.conversionRate(conversionRate.exchangeRate())
				.build();

		if (checkAccountBalance(withdrawAccount.getId()).compareTo(transaction.depositAmount()) < 0) {
			return null;
		}

		return repository.save(transactionDbo);
	}

	private CurrencyExchangeResult callCurrencyClient(CurrencyExchangeRequest request) {
		return client.getCurrencyExchange(request)
				.orElseThrow(() -> new ResourceNotFoundException("Currency exchange rate not found"));
	}
}
