package cz.muni.fi.obs.rest;

import cz.muni.fi.obs.IntegrationTest;
import cz.muni.fi.obs.api.CurrencyExchangeResult;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.controller.pagination.PagedResponse;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.dbo.TransactionState;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import cz.muni.fi.obs.http.CurrencyServiceClient;
import cz.muni.fi.obs.jms.JmsProducer;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static cz.muni.fi.obs.controller.TransactionController.TRANSACTION_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestMethodOrder(OrderAnnotation.class)
class TransactionControllerITTest extends IntegrationTest {

    private static final String SERVICE_API_PATH = "/api/transaction-service";
    private static final String TRANSACTION_CONTROLLER_PATH = SERVICE_API_PATH + TRANSACTION_PATH;

    private boolean setupDone = false;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @MockBean
    private CurrencyServiceClient currencyServiceClient;

    @BeforeEach
    public void setUp() {
        if (setupDone) {
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
        AccountDbo account3 = AccountDbo.builder()
                .id("3")
                .customerId("customer-3")
                .currencyCode("USD")
                .accountNumber("account-3")
                .build();
        AccountDbo account4 = AccountDbo.builder()
                .id("4")
                .customerId("customer-4")
                .currencyCode("CZK")
                .accountNumber("account-4")
                .build();
        AccountDbo account5 = AccountDbo.builder()
                .id("5")
                .customerId("customer-5")
                .currencyCode("EUR")
                .accountNumber("account-5")
                .build();
        AccountDbo account6 = AccountDbo.builder()
                .id("6")
                .customerId("customer-6")
                .currencyCode("EUR")
                .accountNumber("account-6")
                .build();

        TransactionDbo transaction1 = TransactionDbo.builder()
                .id("1")
                .conversionRate(0.04)
                .withdrawsFrom(account1)
                .depositsTo(account2)
                .withdrawAmount(BigDecimal.valueOf(1000))
                .depositAmount(BigDecimal.valueOf(40))
                .note("note-1")
                .variableSymbol("00")
                .transactionState(TransactionState.SUCCESSFUL)
                .build();
        TransactionDbo transaction2 = TransactionDbo.builder()
                .id("2")
                .conversionRate(25.0)
                .withdrawsFrom(account3)
                .depositsTo(account1)
                .withdrawAmount(BigDecimal.valueOf(100))
                .depositAmount(BigDecimal.valueOf(2500))
                .note("note-2")
                .variableSymbol("11")
                .transactionState(TransactionState.SUCCESSFUL)
                .build();
        TransactionDbo transaction3 = TransactionDbo.builder()
                .id("3")
                .conversionRate(25.0)
                .withdrawsFrom(account2)
                .depositsTo(account4)
                .withdrawAmount(BigDecimal.valueOf(50))
                .depositAmount(BigDecimal.valueOf(1250))
                .note("note-3")
                .variableSymbol("22")
                .transactionState(TransactionState.SUCCESSFUL)
                .build();
        TransactionDbo transaction4 = TransactionDbo.builder()
                .id("4")
                .conversionRate(1.0)
                .withdrawsFrom(account5)
                .depositsTo(account3)
                .withdrawAmount(BigDecimal.valueOf(100))
                .depositAmount(BigDecimal.valueOf(100))
                .note("note-4")
                .variableSymbol("33")
                .transactionState(TransactionState.SUCCESSFUL)
                .build();
        TransactionDbo transaction5 = TransactionDbo.builder()
                .id("5")
                .conversionRate(0.04)
                .withdrawsFrom(account4)
                .depositsTo(account2)
                .withdrawAmount(BigDecimal.valueOf(200))
                .depositAmount(BigDecimal.valueOf(8))
                .note("note-5")
                .variableSymbol("44")
                .transactionState(TransactionState.SUCCESSFUL)
                .build();
        TransactionDbo transaction6 = TransactionDbo.builder()
                .id("6")
                .conversionRate(1.0)
                .withdrawsFrom(account3)
                .depositsTo(account5)
                .withdrawAmount(BigDecimal.valueOf(1200))
                .depositAmount(BigDecimal.valueOf(1200))
                .note("note-6")
                .variableSymbol("55")
                .transactionState(TransactionState.SUCCESSFUL)
                .build();
        TransactionDbo transaction7 = TransactionDbo.builder()
                .id("7")
                .conversionRate(1.0)
                .withdrawsFrom(account1)
                .depositsTo(account4)
                .withdrawAmount(BigDecimal.valueOf(700))
                .depositAmount(BigDecimal.valueOf(700))
                .note("note-7")
                .variableSymbol("66")
                .transactionState(TransactionState.SUCCESSFUL)
                .build();
        TransactionDbo transaction8 = TransactionDbo.builder()
                .id("8")
                .conversionRate(1.0)
                .withdrawsFrom(account2)
                .depositsTo(account3)
                .withdrawAmount(BigDecimal.valueOf(150))
                .depositAmount(BigDecimal.valueOf(150))
                .note("note-8")
                .variableSymbol("77")
                .transactionState(TransactionState.SUCCESSFUL)
                .build();
        TransactionDbo transaction9 = TransactionDbo.builder()
                .id("9")
                .conversionRate(25.0)
                .withdrawsFrom(account5)
                .depositsTo(account1)
                .withdrawAmount(BigDecimal.valueOf(400))
                .depositAmount(BigDecimal.valueOf(10000))
                .note("note-9")
                .variableSymbol("88")
                .transactionState(TransactionState.SUCCESSFUL)
                .build();


        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);
        accountRepository.save(account4);
        accountRepository.save(account5);
        accountRepository.save(account6);

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);
        transactionRepository.save(transaction4);
        transactionRepository.save(transaction5);
        transactionRepository.save(transaction6);
        transactionRepository.save(transaction7);
        transactionRepository.save(transaction8);
        transactionRepository.save(transaction9);

        setupDone = true;
    }

    @Order(1)
    @ParameterizedTest
    @MethodSource("provideAccountNumbersAndBalances")
    public void calculateBalanceForAccountNumber_balanceCalculated_returnsBalance(String accountNumber, BigDecimal expectedBalance) {
        UriComponents components = UriComponentsBuilder.fromPath(buildBalancePath(accountNumber)).build();

        BigDecimal balance = requestSpecification(components)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(BigDecimal.class);

        assertThat(balance).isEqualTo(expectedBalance);
    }

    @Order(2)
    @Test
    public void calculateBalanceForAccountNumber_balanceNotFound_returnsZero() {
        UriComponents components = UriComponentsBuilder.fromPath(buildBalancePath("account-6")).build();

        BigDecimal balance = requestSpecification(components)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(BigDecimal.class);

        assertThat(balance).isZero();
    }

    @Order(3)
    @ParameterizedTest
    @MethodSource("provideAccountNumbersAndTransactionCounts")
    public void getTransactionHistory_historyFound_returnsHistory(String accountNumber, int count) {
        UriComponents components = UriComponentsBuilder.fromPath(buildHistoryPath(accountNumber))
                .queryParam("pageNumber", 0)
                .queryParam("pageSize", 10)
                .build();

        PagedResponse<TransactionDbo> transactions = requestSpecification(components)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(new TypeRef<>() {
                });

        assertThat(transactions.records()).hasSize(count);
    }

    @Order(4)
    @Test
    public void getTransactionHistory_historyNotFound_returns404() {
        UriComponents components = UriComponentsBuilder.fromPath(buildHistoryPath("not-existing"))
                .queryParam("pageNumber", 0)
                .queryParam("pageSize", 10)
                .build();

        requestSpecification(components)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get()
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Order(100)
    @Test
    public void createTransaction_correctRequest_createsTransaction() throws InterruptedException {
        prepareTheCurrencyClient();
        UriComponents components = UriComponentsBuilder
                .fromPath(TRANSACTION_CONTROLLER_PATH + "/transaction/create")
                .build();
        TransactionCreateDto transactionCreateDto = TransactionCreateDto.builder()
                .depositsToAccountNumber("account-1")
                .withdrawsFromAccountNumber("account-5")
                .withdrawAmount(BigDecimal.valueOf(10000, 2))
                .note("note")
                .variableSymbol("123")
                .build();

        TransactionDbo result = requestSpecification(components)
                .contentType(ContentType.JSON)
                .body(transactionCreateDto)
                .post()
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(TransactionDbo.class);

        // lazy implementation of waiting for broker:
        Integer queueSize = 1;

        while (queueSize > 0) {
            queueSize = jmsTemplate.browse(JmsProducer.TRANSACTION_QUEUE_NAME, (session, browser) -> Collections.list(browser.getEnumeration()).size());
            Thread.sleep(1000);
        }

        Optional<TransactionDbo> transaction = transactionRepository.findById(result.getId());
        assertThat(transaction).isPresent();
        assertThat(transaction.get())
                .returns(transactionCreateDto.note(), TransactionDbo::getNote)
                .returns(transactionCreateDto.variableSymbol(), TransactionDbo::getVariableSymbol)
                .returns(transactionCreateDto.withdrawAmount(), TransactionDbo::getWithdrawAmount);


        assertThat(transaction.get().getTransactionState()).isIn(TransactionState.SUCCESSFUL, TransactionState.FAILED);
        transactionRepository.deleteById(transaction.get().getId());
    }

    @Order(6)
    @Test
    public void createTransaction_incorrectRequest_returns400() {
        prepareTheCurrencyClient();
        UriComponents components = UriComponentsBuilder
                .fromPath(TRANSACTION_CONTROLLER_PATH + "/transaction/create")
                .build();
        TransactionCreateDto transactionCreateDto = TransactionCreateDto.builder()
                .depositsToAccountNumber("")
                .withdrawsFromAccountNumber("account-2")
                .withdrawAmount(BigDecimal.valueOf(1000))
                .note("note")
                .variableSymbol("123")
                .build();

        requestSpecification(components)
                .contentType(ContentType.JSON)
                .body(transactionCreateDto)
                .post()
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Order(7)
    @Test
    public void createTransaction_insufficientBalance_createsFailedTransaction() throws InterruptedException {
        prepareTheCurrencyClient();
        UriComponents components = UriComponentsBuilder
                .fromPath(TRANSACTION_CONTROLLER_PATH + "/transaction/create")
                .build();
        TransactionCreateDto transactionCreateDto = TransactionCreateDto.builder()
                .depositsToAccountNumber("account-1")
                .withdrawsFromAccountNumber("account-5")
                .withdrawAmount(BigDecimal.valueOf(100000, 2))
                .note("note")
                .variableSymbol("123")
                .build();

        requestSpecification(components)
                .contentType(ContentType.JSON)
                .body(transactionCreateDto)
                .post()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        waitForQueue();

        List<TransactionDbo> accountFiveWithdrawals = transactionRepository.findAll()
                .stream()
                .filter(transactionDbo -> transactionDbo.getWithdrawsFrom().getAccountNumber().equals("account-5"))
                .toList();

        assertThat(accountFiveWithdrawals.stream()
                .anyMatch(transaction -> transaction.getTransactionState().equals(TransactionState.FAILED))).isTrue();
    }

    private String buildBalancePath(String accountNumber) {
        return TRANSACTION_CONTROLLER_PATH + "/account/" + accountNumber + "/balance";
    }

    private String buildHistoryPath(String accountNumber) {
        return TRANSACTION_CONTROLLER_PATH + "/account/" + accountNumber;
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

    private static Stream<Arguments> provideAccountNumbersAndTransactionCounts() {
        return Stream.of(
                Arguments.of("account-1", 4),
                Arguments.of("account-2", 4),
                Arguments.of("account-3", 4),
                Arguments.of("account-4", 3),
                Arguments.of("account-5", 3));
    }

    private static Stream<Arguments> provideAccountNumbersAndBalances() {
        return Stream.of(
                Arguments.of("account-1", BigDecimal.valueOf(1080000, 2)),
                Arguments.of("account-2", BigDecimal.valueOf(-15200, 2)),
                Arguments.of("account-3", BigDecimal.valueOf(-105000, 2)),
                Arguments.of("account-4", BigDecimal.valueOf(175000, 2)),
                Arguments.of("account-5", BigDecimal.valueOf(70000, 2)));
    }
}
