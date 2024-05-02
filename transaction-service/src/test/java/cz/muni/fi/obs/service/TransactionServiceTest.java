package cz.muni.fi.obs.service;

import cz.muni.fi.obs.TestData;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import cz.muni.fi.obs.jms.JmsConsumer;
import cz.muni.fi.obs.jms.JmsProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@Mock
	TransactionRepository repository;

	@Mock
	AccountRepository accountRepository;

	@InjectMocks
	TransactionService transactionService;

	@Mock
	JmsConsumer jmsConsumer;

	@Mock
	private JmsProducer jmsProducer;

	@Test
	public void checkAccountBalance_noTransactions_CalculatesCorrectly() {
        when(repository.findTransactionsDboByWithdrawsFrom_Id(TestData.accountId)).thenReturn(Collections.emptyList());
        when(repository.findTransactionsDboByDepositsTo_Id(TestData.accountId)).thenReturn(Collections.emptyList());
		BigDecimal balance = transactionService.calculateAccountBalance(TestData.accountId);

		assertThat(balance).isEqualTo(BigDecimal.valueOf(0));
	}

	@Test
	public void checkAccountBalance_singleWithdraw_calculatesCorrectly() {
        when(repository.findTransactionsDboByWithdrawsFrom_Id(TestData.accountId)).thenReturn(
				List.of(TestData.withdrawTransactions.getFirst()));
        when(repository.findTransactionsDboByDepositsTo_Id(TestData.accountId)).thenReturn(Collections.emptyList());
		BigDecimal balance = transactionService.calculateAccountBalance(TestData.accountId);

		assertThat(balance).isEqualTo(BigDecimal.valueOf(-1000));
	}

	@Test
	public void checkAccountBalance_singleDeposit_calculatesCorrectly() {
        when(repository.findTransactionsDboByWithdrawsFrom_Id(TestData.accountId)).thenReturn(Collections.emptyList());
        when(repository.findTransactionsDboByDepositsTo_Id(TestData.accountId)).thenReturn(
				List.of(TestData.depositTransactions.getFirst()));
		BigDecimal balance = transactionService.calculateAccountBalance(TestData.accountId);

		assertThat(balance).isEqualTo(BigDecimal.valueOf(3001.5));
	}

	@Test
	public void checkAccountBalance_multipleTransactions_calculatesCorrectly() {
        when(repository.findTransactionsDboByWithdrawsFrom_Id(TestData.accountId)).thenReturn(
				TestData.withdrawTransactions);
        when(repository.findTransactionsDboByDepositsTo_Id(TestData.accountId)).thenReturn(
				TestData.depositTransactions);
		BigDecimal balance = transactionService.calculateAccountBalance(TestData.accountId);

		assertThat(balance).isEqualTo(BigDecimal.valueOf(2043.5));
	}

	@Test
	void viewTransactionHistory_returnsHistory() {
		var pageRequest = PageRequest.of(0, 10);
		when(repository.findTransactionHistory(TestData.accountId, pageRequest))
				.thenReturn(new PageImpl<>(TestData.withdrawTransactions));

		Page<TransactionDbo> actualPage = transactionService.viewTransactionHistory(TestData.accountId, 0, 10);

		assertThat(actualPage.getContent()).isEqualTo(TestData.withdrawTransactions);
		assertThat(actualPage.getTotalPages()).isEqualTo(1);
		assertThat(actualPage.getTotalElements()).isEqualTo(2);
	}

	@Test
	void getTransactionById_returnsTransaction() {
		String transactionId = TestData.withdrawTransactions.getFirst().getId();
		when(repository.findById(transactionId)).thenReturn(Optional.of(TestData.withdrawTransactions.getFirst()));

		Optional<TransactionDbo> actualTransaction = transactionService.getTransactionById(transactionId);
		assertThat(actualTransaction).isPresent();
		assertThat(actualTransaction.get()).isEqualTo(TestData.withdrawTransactions.getFirst());
	}

	@Test
	void createTransaction_createsTransaction() {
		when(repository.save(any())).thenReturn(TestData.withdrawTransactions.getFirst());
		when(accountRepository.findById(any())).thenReturn(Optional.of(new AccountDbo()));

		TransactionDbo createdTransaction = transactionService.createTransaction(TestData.transactionCreateDto());

		verify(repository).save(any());
		assertThat(createdTransaction).isEqualTo(TestData.withdrawTransactions.getFirst());
	}
}
