package cz.muni.fi.obs.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import cz.muni.fi.obs.TestData;
import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.jms.JmsConsumer;
import cz.muni.fi.obs.jms.JmsProducer;
import cz.muni.fi.obs.service.AccountService;
import cz.muni.fi.obs.service.TransactionService;

@ExtendWith(MockitoExtension.class)
class TransactionManagementFacadeTest {

	@Mock
	TransactionService transactionService;

	@Mock
	AccountService accountService;

    @Mock
    JmsProducer jmsProducer;

    @Mock
	JmsConsumer jmsConsumer;

	@InjectMocks
	TransactionManagementFacade transactionManagementFacade;

	@Test
	void getTransactionById_returnsTransaction() {
		String transactionId = TestData.withdrawTransactions.getFirst().getId();
		when(transactionService.getTransactionById(transactionId))
				.thenReturn(Optional.of(TestData.withdrawTransactions.getFirst()));

		Optional<TransactionDbo> actualTransaction = transactionManagementFacade.getTransactionById(transactionId);
		assertThat(actualTransaction).isPresent();
		assertThat(actualTransaction.get()).isEqualTo(TestData.withdrawTransactions.getFirst());
	}

	@Test
	public void createTransaction_createsTransaction() {
		TransactionCreateDto transactionCreateDto = new TransactionCreateDto(
				TestData.withdrawTransactions.getFirst().getWithdrawsFrom().getAccountNumber(),
				TestData.withdrawTransactions.getFirst().getDepositsTo().getAccountNumber(),
				TestData.withdrawTransactions.getFirst().getWithdrawAmount(),
				TestData.withdrawTransactions.getFirst().getNote(),
				TestData.withdrawTransactions.getFirst().getVariableSymbol()
		);

        when(transactionService.createTransaction(transactionCreateDto)).thenReturn(new TransactionDbo());
        doNothing().when(jmsProducer).sendMessage(any(String.class));
		transactionManagementFacade.createTransaction(transactionCreateDto);
		Mockito.verify(transactionService).createTransaction(transactionCreateDto);
	}

	@Test
	void viewTransactionHistory_returnsHistory() {
		when(transactionService.viewTransactionHistory(TestData.accountId, 0, 10))
				.thenReturn(new PageImpl<>(TestData.withdrawTransactions));
		when(accountService.findAccountByAccountNumber("00000001"))
				.thenReturn(AccountDbo.builder()
						.id(TestData.accountId)
						.currencyCode("CZK")
						.accountNumber(1)
						.build());

		Page<TransactionDbo> actualPage = transactionManagementFacade
				.viewTransactionHistory("00000001", 0, 10);
		assertEquals(new PageImpl<>(TestData.withdrawTransactions), actualPage);
	}

	@Test
	public void checkAccountBalance_returnsBalance() {
		when(transactionService.calculateAccountBalance(TestData.accountId)).thenReturn(BigDecimal.valueOf(42));
		when(accountService.findAccountByAccountNumber("00000001"))
				.thenReturn(AccountDbo.builder()
						.id(TestData.accountId)
						.currencyCode("CZK")
						.accountNumber(1)
						.build());

		BigDecimal balance = transactionManagementFacade.checkAccountBalance("00000001");
		BigDecimal balance = transactionManagementFacade.calculateAccountBalance("1234567890");

		assertEquals(BigDecimal.valueOf(42), balance);
	}

	@Test
	public void createAccount_createsAccount() {
		AccountCreateDto accountCreateDto =
				new AccountCreateDto("owner", "CZK", "1234567890");

		AccountDbo accountDbo = AccountDbo.builder()
				.id("1")
				.customerId("owner")
				.currencyCode("CZK")
				.accountNumber("1234567890")
				.build();
		when(accountService.createAccount(accountCreateDto)).thenReturn(accountDbo);

		AccountDbo createdAccount = transactionManagementFacade.createAccount(accountCreateDto);

		verify(accountService).createAccount(accountCreateDto);
		assertThat(createdAccount).isEqualTo(accountDbo);
	}

	@Test
	public void findAccountById_returnsAccount() {
		AccountDbo expectedAccount = AccountDbo.builder()
				.id("1")
				.customerId("owner")
				.currencyCode("CZK")
				.accountNumber(1)
				.build();

		when(accountService.findAccountByAccountNumber("00000001")).thenReturn(expectedAccount);

		AccountDbo foundAccount = transactionManagementFacade.findAccountByAccountNumber("00000001");
		verify(accountService).findAccountByAccountNumber("00000001");
		assertThat(foundAccount).isNotNull();
	}

	@Test
	void testFindAccountById_nonExistingId_returnsNull() {
		when(accountService.findAccountByAccountNumber("0")).thenReturn(null);

		AccountDbo foundAccount = transactionManagementFacade.findAccountByAccountNumber("0");
		verify(accountService).findAccountByAccountNumber("0");
		assertThat(foundAccount).isNull();
	}
}
