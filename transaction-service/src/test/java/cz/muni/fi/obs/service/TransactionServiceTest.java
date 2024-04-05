package cz.muni.fi.obs.service;

import cz.muni.fi.obs.TestData;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import cz.muni.fi.obs.web.CurrencyServiceClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    TransactionRepository repository;

    @Mock
    CurrencyServiceClient client;

    @InjectMocks
    TransactionService transactionService;

    @Test
    public void checkAccountBalance_noTransactions_CalculatesCorrectly() {
        when(repository.getTransactionsByWithdrawId(TestData.accountId)).thenReturn(Collections.emptyList());
        when(repository.getTransactionsByDepositId(TestData.accountId)).thenReturn(Collections.emptyList());
        BigDecimal balance = transactionService.checkAccountBalance(TestData.accountId);

        assertEquals(BigDecimal.valueOf(0), balance);
    }

    @Test
    public void checkAccountBalance_singleWithdraw_calculatesCorrectly() {
        when(repository.getTransactionsByWithdrawId(TestData.accountId)).thenReturn(List.of(TestData.withdrawTransactions.getFirst()));
        when(repository.getTransactionsByDepositId(TestData.accountId)).thenReturn(Collections.emptyList());
        BigDecimal balance = transactionService.checkAccountBalance(TestData.accountId);

        assertEquals(BigDecimal.valueOf(-1000), balance);
    }

    @Test
    public void checkAccountBalance_singleDeposit_calculatesCorrectly() {
        when(repository.getTransactionsByWithdrawId(TestData.accountId)).thenReturn(Collections.emptyList());
        when(repository.getTransactionsByDepositId(TestData.accountId)).thenReturn(List.of(TestData.depositTransactions.getFirst()));
        BigDecimal balance = transactionService.checkAccountBalance(TestData.accountId);

        assertEquals(BigDecimal.valueOf(3001.5), balance);
    }

    @Test
    public void checkAccountBalance_multipleTransactions_calculatesCorrectly() {
        when(repository.getTransactionsByWithdrawId(TestData.accountId)).thenReturn(TestData.withdrawTransactions);
        when(repository.getTransactionsByDepositId(TestData.accountId)).thenReturn(TestData.depositTransactions);
        BigDecimal balance = transactionService.checkAccountBalance(TestData.accountId);

        assertEquals(BigDecimal.valueOf(2043.5), balance);
    }

    @Test
    void viewTransactionHistory_returnsHistory() {
        when(repository.getTransactionHistory(TestData.accountId, 0, 10)).thenReturn(new PageImpl<>(TestData.withdrawTransactions));

        Page<TransactionDbo> actualPage = transactionService.viewTransactionHistory(TestData.accountId, 0, 10);
        assertEquals(new PageImpl<>(TestData.withdrawTransactions), actualPage);
    }

    @Test
    void getTransactionById_returnsTransaction() {
        String transactionId = TestData.withdrawTransactions.getFirst().id();
        when(repository.getTransactionById(transactionId)).thenReturn(TestData.withdrawTransactions.getFirst());

        TransactionDbo actualTransaction = transactionService.getTransactionById(transactionId);
        assertEquals(TestData.withdrawTransactions.getFirst(), actualTransaction);
    }

    @Test
    void createTransaction_createsTransaction() {
        when(client.getConversionRate()).thenCallRealMethod();
        TransactionCreateDto transactionCreateDto = new TransactionCreateDto(
                TestData.withdrawTransactions.getFirst().withdrawsFrom(),
                TestData.withdrawTransactions.getFirst().depositsTo(),
                TestData.withdrawTransactions.getFirst().withdrawAmount(),
                TestData.withdrawTransactions.getFirst().depositAmount(),
                TestData.withdrawTransactions.getFirst().note(),
                TestData.withdrawTransactions.getFirst().variableSymbol()
        );

        Mockito.doNothing().when(repository).createTransaction(any(TransactionDbo.class));


        transactionService.createTransaction(transactionCreateDto);

        Mockito.verify(repository, Mockito.times(1)).createTransaction(any(TransactionDbo.class));
    }
}
