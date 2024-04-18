package cz.muni.fi.obs.facade;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.service.AccountService;
import cz.muni.fi.obs.service.TransactionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TransactionManagementFacade {

	private final TransactionService transactionService;
	private final AccountService accountService;

	@Autowired
	public TransactionManagementFacade(TransactionService transactionService, AccountService accountService) {
		this.transactionService = transactionService;
		this.accountService = accountService;
	}

	public Optional<TransactionDbo> getTransactionById(String id) {
		return transactionService.getTransactionById(id);
	}

	public void createTransaction(TransactionCreateDto transaction) {
		transactionService.createTransaction(transaction);
	}

	public Page<TransactionDbo> viewTransactionHistory(String accountNumber, int pageNumber, int pageSize) {
		AccountDbo account = accountService.findAccountByAccountNumber(accountNumber)
				.orElseThrow(() -> {
					log.info("Account not found: {}", accountNumber);
					return new ResourceNotFoundException("Account not found");
				});
		return transactionService.viewTransactionHistory(account.getId(), pageNumber, pageSize);
	}

	public BigDecimal checkAccountBalance(String accountNumber) {
		AccountDbo account = accountService.findAccountByAccountNumber(accountNumber)
				.orElseThrow(() -> {
					log.info("Account not found: {}", accountNumber);
					return new ResourceNotFoundException("Account not found");
				});
		return transactionService.checkAccountBalance(account.getId());
	}

	public void createAccount(AccountCreateDto accountCreateDto) {
		accountService.createAccount(accountCreateDto);
	}

	public Optional<AccountDbo> findAccountByAccountNumber(String accountNumber) {
		return accountService.findAccountByAccountNumber(accountNumber);
	}
}

