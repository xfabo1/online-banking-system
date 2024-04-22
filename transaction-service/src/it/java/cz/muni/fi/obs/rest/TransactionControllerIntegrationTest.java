package cz.muni.fi.obs.rest;

import static cz.muni.fi.obs.controller.TransactionController.TRANSACTION_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.MethodOrderer;
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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import cz.muni.fi.obs.ControllerIntegrationTest;
import cz.muni.fi.obs.api.CurrencyExchangeResult;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.controller.pagination.PagedResponse;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import cz.muni.fi.obs.http.CurrencyServiceClient;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

@TestMethodOrder(OrderAnnotation.class)
class TransactionControllerIntegrationTest extends ControllerIntegrationTest {

	private static final String SERVICE_API_PATH = "/api/transaction-service";
	private static final String TRANSACTION_CONTROLLER_PATH = SERVICE_API_PATH + TRANSACTION_PATH;

	@Autowired
	private TransactionRepository transactionRepository;
	@MockBean
	private CurrencyServiceClient currencyServiceClient;

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
				.as(new TypeRef<>() {});

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
	public void createTransaction_correctRequest_createsTransaction() {
		prepareTheCurrencyClient();
		UriComponents components = UriComponentsBuilder
				.fromPath(TRANSACTION_CONTROLLER_PATH + "/transaction/create")
				.build();
		TransactionCreateDto transactionCreateDto = TransactionCreateDto.builder()
				.depositsToAccountNumber("account-1")
				.withdrawsFromAccountNumber("account-5")
				.depositAmount(BigDecimal.valueOf(10000, 2))
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

		Optional<TransactionDbo> transaction = transactionRepository.findById(result.getId());
		assertThat(transaction).isPresent();
		assertThat(transaction.get())
				.returns(transactionCreateDto.note(), TransactionDbo::getNote)
				.returns(transactionCreateDto.variableSymbol(), TransactionDbo::getVariableSymbol)
				.returns(transactionCreateDto.depositAmount(), TransactionDbo::getDepositAmount)
				.returns(transactionCreateDto.withdrawAmount(), TransactionDbo::getWithdrawAmount);

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
				.depositAmount(BigDecimal.valueOf(1000))
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
	public void createTransaction_insufficientBalance_returns409() {
		prepareTheCurrencyClient();
		UriComponents components = UriComponentsBuilder
				.fromPath(TRANSACTION_CONTROLLER_PATH + "/transaction/create")
				.build();
		TransactionCreateDto transactionCreateDto = TransactionCreateDto.builder()
				.depositsToAccountNumber("account-1")
				.withdrawsFromAccountNumber("account-5")
				.depositAmount(BigDecimal.valueOf(100000, 2))
				.withdrawAmount(BigDecimal.valueOf(100000, 2))
				.note("note")
				.variableSymbol("123")
				.build();

		requestSpecification(components)
				.contentType(ContentType.JSON)
				.body(transactionCreateDto)
				.post()
				.then()
				.statusCode(HttpStatus.SC_CONFLICT);
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
				Arguments.of("account-2",  4),
				Arguments.of("account-3",  4),
				Arguments.of("account-4",  3),
				Arguments.of("account-5",  3));
	}

	private static Stream<Arguments> provideAccountNumbersAndBalances() {
		return Stream.of(
				Arguments.of("account-1", BigDecimal.valueOf(1080000, 2)),
				Arguments.of("account-2",  BigDecimal.valueOf(-15200, 2)),
				Arguments.of("account-3",  BigDecimal.valueOf(-105000, 2)),
				Arguments.of("account-4",  BigDecimal.valueOf(175000, 2)),
				Arguments.of("account-5",  BigDecimal.valueOf(70000, 2)));
	}
}
