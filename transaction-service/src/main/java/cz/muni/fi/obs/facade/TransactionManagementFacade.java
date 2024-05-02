package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.api.TransactionDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.jms.JmsProducer;
import cz.muni.fi.obs.service.AccountService;
import cz.muni.fi.obs.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
		TransactionDbo createdTransaction = transactionService.createTransaction(transaction);
		jmsProducer.sendMessage(createdTransaction.getId());
		return createdTransaction;
	}

	public Page<TransactionDbo> viewTransactionHistory(String accountNumber, int pageNumber, int pageSize) {
		AccountDbo account = findAccountByAccountNumber(accountNumber);
		return transactionService.viewTransactionHistory(account.getId(), pageNumber, pageSize);
	}

	public BigDecimal calculateAccountBalance(String accountNumber) {
		AccountDbo account = findAccountByAccountNumber(accountNumber);
		return transactionService.calculateAccountBalance(account.getId());
	}

	public AccountDbo createAccount(AccountCreateDto accountCreateDto) {
		return accountService.createAccount(accountCreateDto);
	}

	public AccountDbo findAccountByAccountNumber(String accountNumber) {
		return accountService.findAccountByAccountNumber(accountNumber);
	}

	public List<AccountDbo> findAccountsByCustomerId(String customerId) {
		return accountService.findAccountsByCustomerId(customerId);
	}

    public Page<AccountDbo> listAccounts(Pageable pageable) {
        return accountService.listAccounts(pageable);
    }

	public Page<TransactionDto> listTransactions(String accountId, int pageNumber, int pageSize, LocalDate date) {
		Page<TransactionDbo> transactionDbos = transactionService.listByAccount(accountId, Pageable.ofSize(pageSize).withPage(pageNumber), date);
		return new PageImpl<>(transactionDbos.getContent().stream().map(TransactionDto::fromDbo).collect(Collectors.toList()),
				Pageable.ofSize(pageSize).withPage(pageNumber),
				transactionDbos.getTotalElements());
	}
}

