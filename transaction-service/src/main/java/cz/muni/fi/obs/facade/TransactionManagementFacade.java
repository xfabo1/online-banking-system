package cz.muni.fi.obs.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.service.AccountService;
import cz.muni.fi.obs.service.TransactionService;

@Component
public class TransactionManagementFacade {

	private final TransactionService transactionService;
	private final AccountService accountService;

	@Autowired
	public TransactionManagementFacade(TransactionService transactionService, AccountService accountService) {
		this.transactionService = transactionService;
		this.accountService = accountService;
	}

	public TransactionDbo getTransactionById(String id) {
		return transactionService.getTransactionById(id);
	}

	public void createTransaction(TransactionCreateDto transaction) {
		transactionService.createTransaction(transaction);
	}

	public List<TransactionDbo> viewTransactionHistory(String accountId) {
		return transactionService.viewTransactionHistory(accountId);
	}

	public long checkAccountBalance(String accountId) {
		return transactionService.checkAccountBalance(accountId);
	}

	public void createAccount(AccountCreateDto accountCreateDto) {
		accountService.createAccount(accountCreateDto);
	}
}

