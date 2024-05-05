package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.CurrencyExchangeRequest;
import cz.muni.fi.obs.api.CurrencyExchangeResult;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.dbo.TransactionState;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.http.CurrencyServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static cz.muni.fi.obs.data.dbo.TransactionState.FAILED;
import static cz.muni.fi.obs.data.dbo.TransactionState.PENDING;
import static cz.muni.fi.obs.data.dbo.TransactionState.SUCCESSFUL;

@Slf4j
@Service
@Transactional
public class TransactionService {

	private final TransactionRepository repository;
	private final CurrencyServiceClient client;
	private final AccountRepository accountRepository;
	private final List<String> bankAccounts;

	@Autowired
	public TransactionService(TransactionRepository repository,
							  CurrencyServiceClient client,
							  AccountRepository accountRepository,
							  @Value("${bank.bank-accounts}") List<String> bankAccounts) {
		this.repository = repository;
		this.client = client;
		this.accountRepository = accountRepository;
		this.bankAccounts = bankAccounts;
	}

	public BigDecimal calculateAccountBalance(String accountId) {
		var withdraws = repository.findTransactionsDboByWithdrawsFrom_Id(accountId);
		var deposits = repository.findTransactionsDboByDepositsTo_Id(accountId);

		BigDecimal withdrawSum = withdraws.stream()
				.filter(transactionDbo -> transactionDbo.getTransactionState().equals(SUCCESSFUL))
				.map(TransactionDbo::getWithdrawAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal depositSum = deposits.stream()
				.filter(transactionDbo -> transactionDbo.getTransactionState().equals(SUCCESSFUL))
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

	public TransactionDbo createTransaction(TransactionCreateDto transaction) {
		AccountDbo withdrawsFromAccount = accountRepository.findAccountDboByAccountNumber(transaction.withdrawsFromAccountNumber())
				.orElseThrow(() -> new ResourceNotFoundException(AccountDbo.class, transaction.withdrawsFromAccountNumber()));
		AccountDbo depositsToAccount = accountRepository.findAccountDboByAccountNumber(transaction.depositsToAccountNumber())
				.orElseThrow(() -> new ResourceNotFoundException(AccountDbo.class, transaction.depositsToAccountNumber()));

		TransactionDbo transactionDbo = TransactionDbo.builder()
				.id(UUID.randomUUID().toString())
				.withdrawsFrom(withdrawsFromAccount)
				.note(transaction.note())
				.depositsTo(depositsToAccount)
				.withdrawAmount(transaction.withdrawAmount())
				.variableSymbol(transaction.variableSymbol())
				.transactionState(PENDING)
				.build();

		return repository.save(transactionDbo);
	}

	public void executeTransaction(String transactionId) {
		TransactionDbo transaction = repository.findById(transactionId)
				.orElseThrow(() -> new ResourceNotFoundException(TransactionDbo.class, transactionId));

		// do not call currency service if you don't need to
		if (!transaction.getWithdrawsFrom().getCurrencyCode().equals(transaction.getDepositsTo().getCurrencyCode())) {
			CurrencyExchangeRequest request = CurrencyExchangeRequest.builder()
					.from(transaction.getWithdrawsFrom().getCurrencyCode())
					.to(transaction.getDepositsTo().getCurrencyCode())
					.amount(transaction.getWithdrawAmount())
					.build();

			CurrencyExchangeResult exchangeResult = callCurrencyClient(request);
			transaction.setConversionRate(exchangeResult.exchangeRate());
			transaction.setDepositAmount(exchangeResult.destAmount());
		} else {
			transaction.setConversionRate(1d);
			transaction.setDepositAmount(transaction.getWithdrawAmount());
		}

		// allow sub-zero withdrawals if this is our bank accounts to simulate loaning money as it works in real banks
		if (!bankAccounts.contains(transaction.getWithdrawsFrom().getAccountNumber())) {
			transaction.setTransactionState(computeTransactionState(transaction.getWithdrawsFrom().getId(), transaction.getWithdrawAmount()));
		} else {
			transaction.setTransactionState(SUCCESSFUL);
		}

		repository.save(transaction);
	}

	private TransactionState computeTransactionState(String id, BigDecimal withdrawAmount) {
		return calculateAccountBalance(id).compareTo(withdrawAmount) >= 0 ? SUCCESSFUL : FAILED;
	}

	private CurrencyExchangeResult callCurrencyClient(CurrencyExchangeRequest request) {
		return client.getCurrencyExchange(request)
				.orElseThrow(() -> new ResourceNotFoundException("Currency exchange rate not found"));
	}
}
