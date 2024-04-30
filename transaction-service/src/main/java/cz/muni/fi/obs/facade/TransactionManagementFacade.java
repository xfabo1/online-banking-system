package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.jms.JmsProducer;
import cz.muni.fi.obs.service.AccountService;
import cz.muni.fi.obs.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TransactionManagementFacade {

	private final TransactionService transactionService;
	private final AccountService accountService;
	private final JmsProducer jmsProducer;

	@Autowired
	public TransactionManagementFacade(TransactionService transactionService, AccountService accountService, JmsProducer jmsProducer) {
		this.transactionService = transactionService;
		this.accountService = accountService;
		this.jmsProducer = jmsProducer;
	}

	public Optional<TransactionDbo> getTransactionById(String id) {
		return transactionService.getTransactionById(id);
	}

	public TransactionDbo createTransaction(TransactionCreateDto transaction) {
		return transactionService.createTransaction(transaction);
	}

	public Page<TransactionDbo> viewTransactionHistory(String accountNumber, int pageNumber, int pageSize) {
		AccountDbo account = getAccountByAccountNumber(accountNumber);
		return transactionService.viewTransactionHistory(account.getId(), pageNumber, pageSize);
	}

	public BigDecimal checkAccountBalance(String accountNumber) {
		AccountDbo account = getAccountByAccountNumber(accountNumber);
		return transactionService.calculateAccountBalance(account.getId());
	}

	public AccountDbo createAccount(AccountCreateDto accountCreateDto) {
		return accountService.createAccount(accountCreateDto);
	}

	public Optional<AccountDbo> findAccountByAccountNumber(String accountNumber) {
		return accountService.findAccountByAccountNumber(accountNumber);
	}

	private AccountDbo getAccountByAccountNumber(String accountNumber) {
		return accountService.findAccountByAccountNumber(accountNumber)
				.orElseThrow(() -> {
					log.info("Account not found: {}", accountNumber);
					return new ResourceNotFoundException("Account not found");
				});
	}

	public List<AccountDbo> findAccountsByCustomerId(String customerId) {
		return accountService.findAccountsByCustomerId(customerId);
	}
}

