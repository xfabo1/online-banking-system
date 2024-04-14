package cz.muni.fi.obs.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.repository.TransactionRepository;
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
		var withdraws = repository.getTransactionsByWithdrawId(accountId);
		var deposits = repository.getTransactionsByDepositId(accountId);

		BigDecimal withdrawSum = withdraws.stream()
				.map(TransactionDbo::withdrawAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal depositSum = deposits.stream()
				.map(TransactionDbo::depositAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return depositSum.subtract(withdrawSum);
	}

	public Page<TransactionDbo> viewTransactionHistory(String accountId, int pageNumber, int pageSize) {
		return repository.getTransactionHistory(accountId, pageNumber, pageSize);
	}

	public TransactionDbo getTransactionById(String id) {
		return repository.getTransactionById(id);
	}

	public void createTransaction(TransactionCreateDto transaction) {
		var conversionRate = client.getCurrencyExchange(null);

		var transactionDbo = TransactionDbo.builder()
				.id(UUID.randomUUID().toString())
				.withdrawsFrom(transaction.withdrawsFrom())
				.note(transaction.note())
				.depositsTo(transaction.depositsTo())
				.depositAmount(transaction.depositAmount())
				.withdrawAmount(transaction.withdrawAmount())
				.variableSymbol(transaction.variableSymbol())
				.conversionRate(conversionRate.exchangeRate())
				.build();

		repository.createTransaction(transactionDbo);
	}
}
