package cz.muni.fi.obs.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.controller.PagedResponse;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import cz.muni.fi.obs.web.CurrencyServiceClient;

@Service
public class TransactionService {

	private final TransactionRepository repository;
	private final CurrencyServiceClient client;

	@Autowired
	public TransactionService(TransactionRepository repository, CurrencyServiceClient client) {
		this.repository = repository;
		this.client = client;
	}

	public long checkAccountBalance(String accountId) {
		var withdraws = repository.getTransactionsByWithdrawId(accountId);
		var deposits = repository.getTransactionsByDepositId(accountId);

		long withdrawSum = withdraws.stream().mapToLong(TransactionDbo::withdrawAmount).sum();
		long depositSum = deposits.stream().mapToLong(TransactionDbo::depositAmount).sum();

		return depositSum - withdrawSum;
	}

	public PagedResponse<TransactionDbo> viewTransactionHistory(String accountId, int pageNumber, int pageSize) {
		var page = repository.getTransactionHistory(accountId, pageNumber, pageSize);
		return PagedResponse.fromPage(page);
	}

	public TransactionDbo getTransactionById(String id) {
		return repository.getTransactionById(id);
	}

	public void createTransaction(TransactionCreateDto transaction) {
		var conversionRate = client.getConversionRate();

		var transactionDbo = TransactionDbo.builder()
				.id(UUID.randomUUID().toString())
				.withdrawsFrom(transaction.withdrawsFrom())
				.note(transaction.note())
				.depositsTo(transaction.depositsTo())
				.depositAmount(transaction.depositAmount())
				.withdrawAmount(transaction.withdrawAmount())
				.variableSymbol(transaction.variableSymbol())
				.conversionRate(conversionRate.exchangedRate())
				.state(transaction.state())
				.build();

		repository.createTransaction(transactionDbo);
	}
}
