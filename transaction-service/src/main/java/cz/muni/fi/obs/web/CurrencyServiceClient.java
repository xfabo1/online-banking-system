package cz.muni.fi.obs.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.muni.fi.obs.api.CurrencyExchangeResult;

@Configuration
public class CurrencyServiceClient {

	@Bean
	public CurrencyExchangeResult getConversionRate() {
		return CurrencyExchangeResult.builder()
				.sourceAmount(100)
				.exchangedAmount(4)
				.exchangedRate(25)
				.symbolFrom("CZK")
				.symbolTo("EUR").build();
	}
}
