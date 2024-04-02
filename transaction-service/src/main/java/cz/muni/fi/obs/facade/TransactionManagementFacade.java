package cz.muni.fi.obs.facade;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
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

	public Page<TransactionDbo> viewTransactionHistory(String accountId, int pageNumber, int pageSize) {
		return transactionService.viewTransactionHistory(accountId, pageNumber, pageSize);
	}

	public BigDecimal checkAccountBalance(String accountId) {
		return transactionService.checkAccountBalance(accountId);
	}

	public void createAccount(AccountCreateDto accountCreateDto) {
		accountService.createAccount(accountCreateDto);
	}

	public AccountDbo findAccountById(String id) {
		return accountService.findAccountById(id);
	}
}

