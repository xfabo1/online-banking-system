package cz.muni.fi.obs.http;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JAutoConfiguration;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.http.MediaType;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.Options;

import cz.muni.fi.obs.api.CurrencyExchangeRequest;
import cz.muni.fi.obs.api.CurrencyExchangeResult;
import cz.muni.fi.obs.config.FeignClientConfiguration;
import io.github.resilience4j.springboot3.circuitbreaker.autoconfigure.CircuitBreakerAutoConfiguration;
import io.github.resilience4j.springboot3.timelimiter.autoconfigure.TimeLimiterAutoConfiguration;
import util.JsonConvertor;

@SpringBootTest(
		webEnvironment = NONE,
		classes = FeignClientConfiguration.FeignConfiguration.class,
		properties = "clients.currency-service.url=http://localhost:${wiremock.server.port}"
)
@ImportAutoConfiguration({
		CircuitBreakerAutoConfiguration.class,
		TimeLimiterAutoConfiguration.class,
		Resilience4JAutoConfiguration.class,
		FeignAutoConfiguration.class
})
@AutoConfigureWebMvc
@AutoConfigureWireMock(port = Options.DYNAMIC_PORT)
class CurrencyServiceClientTest {

	private static final String CURRENCY_EXCHANGE_PATH = "/v1/currencies/exchange";

	@Autowired
	private CurrencyServiceClient currencyServiceClient;

	@Test
	void getCurrencyExchange_exchangeCalculated_returningExchange() throws Exception {
		CurrencyExchangeResult result = CurrencyExchangeResult.builder()
				.sourceAmount(BigDecimal.valueOf(100))
				.destAmount(BigDecimal.valueOf(4))
				.exchangeRate(4.0)
				.symbolFrom("CZK")
				.symbolTo("EUR").build();
		CurrencyExchangeRequest request = CurrencyExchangeRequest.builder()
				.from("CZK")
				.to("EUR")
				.amount(BigDecimal.valueOf(100)).build();

		stubFor(post(urlPathEqualTo(CURRENCY_EXCHANGE_PATH))
				.withRequestBody(WireMock.equalTo(JsonConvertor.convertObjectToJson(request)))
				.willReturn(aResponse()
						.withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
						.withBody(JsonConvertor.convertObjectToJson(result))));

		var exchangeResult = currencyServiceClient.getCurrencyExchange(request);

		assertThat(exchangeResult)
				.returns(BigDecimal.valueOf(4), CurrencyExchangeResult::destAmount)
				.returns(BigDecimal.valueOf(100), CurrencyExchangeResult::sourceAmount)
				.returns(4.0, CurrencyExchangeResult::exchangeRate)
				.returns("CZK", CurrencyExchangeResult::symbolFrom)
				.returns("EUR", CurrencyExchangeResult::symbolTo);
	}

	@Test
	public void getCurrencyExchange_errorOccurred_returningNull() {
		CurrencyExchangeRequest request = CurrencyExchangeRequest.builder()
				.from("CZK")
				.to("EUR")
				.amount(BigDecimal.valueOf(100)).build();
		stubFor(post(urlPathEqualTo(CURRENCY_EXCHANGE_PATH))
				.willReturn((serverError())));

		var exchangeResult = currencyServiceClient.getCurrencyExchange(request);

		assertThat(exchangeResult).isNull();
	}
}
