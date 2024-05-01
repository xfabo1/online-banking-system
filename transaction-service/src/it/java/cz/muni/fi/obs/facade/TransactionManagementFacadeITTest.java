package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.IntegrationTest;
import cz.muni.fi.obs.api.CurrencyExchangeResult;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.dbo.TransactionState;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import cz.muni.fi.obs.http.CurrencyServiceClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransactionManagementFacadeITTest extends IntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionManagementFacade facade;

    @Autowired
    private TransactionRepository transactionRepository;

    @MockBean
    private CurrencyServiceClient currencyServiceClient;

    private AccountDbo account1;

    private AccountDbo account2;

    private AccountDbo bank;

    private boolean accountsCreated = false;

    @BeforeEach
    public void setUp() {
        if (accountsCreated) {
            return;
        }

        AccountDbo account1 = AccountDbo.builder()
                .id("1")
                .customerId("customer-1")
                .currencyCode("CZK")
                .accountNumber("account-1")
                .build();
        AccountDbo account2 = AccountDbo.builder()
                .id("2")
                .customerId("customer-2")
                .currencyCode("EUR")
                .accountNumber("account-2")
                .build();
        // this account lets us deposit money to real customer accounts
        AccountDbo bank = AccountDbo.builder()
                .id("3")
                .customerId("bank-1")
                .currencyCode("EUR")
                .accountNumber("bank-1")
                .build();

        this.account1 = accountRepository.save(account1);
        this.account2 = accountRepository.save(account2);
        this.bank = accountRepository.save(bank);

        accountsCreated = true;
    }

    @AfterEach
    public void deleteTransactions() {
        transactionRepository.deleteAll();
        transactionRepository.flush();
    }

    @Test
    public void createThreeTransactions_validTransactions_transactionsAreProcessedToFailed() throws InterruptedException {
        TransactionCreateDto transactionCreateDto = new TransactionCreateDto(account2.getAccountNumber(), account1.getAccountNumber(),
                BigDecimal.valueOf(1000), "", "");

        for (int i = 0; i < 3; i++) {
            prepareTheCurrencyClient();
            facade.createTransaction(transactionCreateDto);
        }

        waitForQueue();

        Page<TransactionDbo> transactionDbos = facade.viewTransactionHistory(account1.getAccountNumber(), 0, 10);

        assertThat(transactionDbos.getContent().stream()
                .allMatch(trans -> trans.getTransactionState().equals(TransactionState.FAILED))).isTrue();
        assertThat(facade.calculateAccountBalance(account1.getAccountNumber())).isEqualTo(BigDecimal.valueOf(0));
        assertThat(facade.calculateAccountBalance(account2.getAccountNumber())).isEqualTo(BigDecimal.valueOf(0));
    }

    @Test
    public void depositMoneyToBankThenTransferToAccount_validTransactions_transactionsAreProccessedToSuccess() throws InterruptedException {
        // deposit 1000 from bank to account2
        TransactionCreateDto transactionCreateDto = new TransactionCreateDto(bank.getAccountNumber(), account2.getAccountNumber(),
                BigDecimal.valueOf(1000), "", "");

        facade.createTransaction(transactionCreateDto);

        waitForQueue();

        Page<TransactionDbo> transactionDbos = facade.viewTransactionHistory(account2.getAccountNumber(), 0, 10);
        assertThat(transactionDbos.getContent().getFirst().getTransactionState()).isEqualTo(TransactionState.SUCCESSFUL);

        // now send 1000 to czech account
        TransactionCreateDto transactionCreateDto1 = new TransactionCreateDto(account2.getAccountNumber(), account1.getAccountNumber(),
                BigDecimal.valueOf(1000), "", "");

        prepareTheCurrencyClient();
        facade.createTransaction(transactionCreateDto1);

        waitForQueue();
        Page<TransactionDbo> account1Transactions = facade.viewTransactionHistory(account1.getAccountNumber(), 0, 10);
        assertThat(account1Transactions.getContent().getFirst().getTransactionState()).isEqualTo(TransactionState.SUCCESSFUL);

        // verify account balances
        assertThat(facade.calculateAccountBalance(bank.getAccountNumber())).isEqualByComparingTo(BigDecimal.valueOf(-1000));
        assertThat(facade.calculateAccountBalance(account2.getAccountNumber())).isEqualByComparingTo(BigDecimal.valueOf(0));
        assertThat(facade.calculateAccountBalance(account1.getAccountNumber())).isEqualByComparingTo(BigDecimal.valueOf(25000));
    }

    private void prepareTheCurrencyClient() {
        CurrencyExchangeResult result = CurrencyExchangeResult.builder()
                .exchangeRate(25.0)
                .destAmount(BigDecimal.valueOf(25000))
                .sourceAmount(BigDecimal.valueOf(1000))
                .symbolFrom("EUR")
                .symbolTo("CZK")
                .build();

        when(currencyServiceClient.getCurrencyExchange(any())).thenReturn(Optional.of(result));
    }
}
