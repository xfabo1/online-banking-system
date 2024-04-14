package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.TestData;
import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.service.AccountService;
import cz.muni.fi.obs.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionManagementFacadeTest {

    @Mock
    TransactionService transactionService;

    @Mock
    AccountService accountService;

    @InjectMocks
    TransactionManagementFacade transactionManagementFacade;

    @Test
    void getTransactionById_returnsTransaction() {
        String transactionId = TestData.withdrawTransactions.getFirst().id();
        when(transactionService.getTransactionById(transactionId)).thenReturn(TestData.withdrawTransactions.getFirst());

        TransactionDbo actualTransaction = transactionManagementFacade.getTransactionById(transactionId);
        assertEquals(TestData.withdrawTransactions.getFirst(), actualTransaction);
    }

    @Test
    public void createTransaction_createsTransaction() {
        TransactionCreateDto transactionCreateDto = new TransactionCreateDto(
                TestData.withdrawTransactions.getFirst().withdrawsFrom(),
                TestData.withdrawTransactions.getFirst().depositsTo(),
                TestData.withdrawTransactions.getFirst().withdrawAmount(),
                TestData.withdrawTransactions.getFirst().depositAmount(),
                TestData.withdrawTransactions.getFirst().note(),
                TestData.withdrawTransactions.getFirst().variableSymbol()
        );

        Mockito.doNothing().when(transactionService).createTransaction(any(TransactionCreateDto.class));


        transactionManagementFacade.createTransaction(transactionCreateDto);

        Mockito.verify(transactionService).createTransaction(transactionCreateDto);
    }

    @Test
    void viewTransactionHistory_returnsHistory() {
        when(transactionService.viewTransactionHistory(TestData.accountId, 0, 10)).thenReturn(new PageImpl<>(TestData.withdrawTransactions));

        Page<TransactionDbo> actualPage = transactionManagementFacade.viewTransactionHistory(TestData.accountId, 0, 10);
        assertEquals(new PageImpl<>(TestData.withdrawTransactions), actualPage);
    }

    @Test
    public void checkAccountBalance_returnsBalance() {
        when(transactionService.checkAccountBalance(TestData.accountId)).thenReturn(BigDecimal.valueOf(42));

        BigDecimal balance = transactionManagementFacade.checkAccountBalance(TestData.accountId);

        assertEquals(BigDecimal.valueOf(42), balance);
    }

    @Test
    public void createAccount_createsAccount() {
        AccountCreateDto accountCreateDto = new AccountCreateDto("owner", "CZK", "1234567890");

        Mockito.doNothing().when(accountService).createAccount(any(AccountCreateDto.class));

        transactionManagementFacade.createAccount(accountCreateDto);

        Mockito.verify(accountService).createAccount(accountCreateDto);
    }

    @Test
    public void findAccountById_returnsAccount() {
        String accountId = "1";
        AccountDbo expectedAccount = AccountDbo.builder()
                .id(accountId)
                .customerId("owner")
                .currencyCode("CZK")
                .accountNumber("1234567890")
                .build();

        when(accountService.findAccountById(accountId)).thenReturn(expectedAccount);

        AccountDbo foundAccount = transactionManagementFacade.findAccountById(accountId);
        verify(accountService).findAccountById(accountId);
        assertEquals(expectedAccount, foundAccount);
    }

    @Test
    void testFindAccountById_nonExistingId_returnsNull() {
        String accountId = "non-existing-id";
        when(accountService.findAccountById(accountId)).thenReturn(null);

        AccountDbo foundAccount = transactionManagementFacade.findAccountById(accountId);
        verify(accountService).findAccountById(accountId);
        assertNull(foundAccount);
    }
}
