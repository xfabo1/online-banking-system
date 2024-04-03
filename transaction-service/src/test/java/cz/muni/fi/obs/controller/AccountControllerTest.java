package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.facade.TransactionManagementFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Mock
    private TransactionManagementFacade facade;
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        accountController = new AccountController(facade);
    }

    @Test
    void createAccount_validRequest_returnsCreated() {
        AccountCreateDto accountCreateDto = new AccountCreateDto("owner", "CZK", "1234567890");
        ResponseEntity<Void> responseEntity = accountController.createAccount(accountCreateDto);

        verify(facade).createAccount(accountCreateDto);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void findAccountById_accountFound_returnsAccount() {
        String accountId = "1";
        AccountDbo expectedAccount = AccountDbo.builder()
                .id("1")
                .customerId("owner")
                .currencyCode("CZK")
                .accountNumber("1234567890").build();

        when(facade.findAccountById(accountId)).thenReturn(expectedAccount);

        ResponseEntity<AccountDbo> responseEntity = accountController.findAccountById(accountId);

        verify(facade).findAccountById(accountId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedAccount, responseEntity.getBody());
    }

    @Test
    void testFindAccountById_nonExistingId_returnsNotFound() {
        String accountId = "1";

        when(facade.findAccountById(accountId)).thenReturn(null);

        ResponseEntity<AccountDbo> responseEntity = accountController.findAccountById(accountId);

        verify(facade).findAccountById(accountId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
}
