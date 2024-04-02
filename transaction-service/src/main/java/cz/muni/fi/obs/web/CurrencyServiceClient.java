package cz.muni.fi.obs.web;

import java.math.BigDecimal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.muni.fi.obs.api.CurrencyExchangeResult;

@Configuration
public class CurrencyServiceClient {

	@Bean
	public CurrencyExchangeResult getConversionRate() {
		return CurrencyExchangeResult.builder()
				.sourceAmount(BigDecimal.valueOf(100))
				.exchangedAmount(BigDecimal.valueOf(4))
				.exchangedRate(BigDecimal.valueOf(4))
				.symbolFrom("CZK")
				.symbolTo("EUR").build();
	}
}
