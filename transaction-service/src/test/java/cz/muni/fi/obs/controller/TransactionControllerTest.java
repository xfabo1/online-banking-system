package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.TestData;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.facade.TransactionManagementFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {


    @Mock
    TransactionManagementFacade transactionManagementFacade;

    @InjectMocks
    TransactionController transactionController;


    @Test
    void getTransactionById() {
        String transactionId = TestData.withdrawTransactions.getFirst().id();
        when(transactionManagementFacade.getTransactionById(transactionId)).thenReturn(TestData.withdrawTransactions.getFirst());

        ResponseEntity<TransactionDbo> responseEntity = transactionController.getTransactionById(transactionId);
        assertEquals(TestData.withdrawTransactions.getFirst(), responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void viewTransactionHistory_returnsHistory() {
        when(transactionManagementFacade.viewTransactionHistory(TestData.accountId, 0, 10)).thenReturn(new PageImpl<>(TestData.withdrawTransactions));

        ResponseEntity<Page<TransactionDbo>> responseEntity = transactionController.viewTransactionHistory(TestData.accountId, 0, 10);

        assertEquals(new PageImpl<>(TestData.withdrawTransactions), responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void checkAccountBalance_returnsBalance() {
        when(transactionManagementFacade.checkAccountBalance(TestData.accountId)).thenReturn(BigDecimal.valueOf(42));

        ResponseEntity<BigDecimal> responseEntity = transactionController.checkAccountBalance(TestData.accountId);

        assertEquals(BigDecimal.valueOf(42), responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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

        Mockito.doNothing().when(transactionManagementFacade).createTransaction(any(TransactionCreateDto.class));


        ResponseEntity<Void> responseEntity = transactionController.createTransaction(transactionCreateDto);

        Mockito.verify(transactionManagementFacade).createTransaction(transactionCreateDto);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }
}
