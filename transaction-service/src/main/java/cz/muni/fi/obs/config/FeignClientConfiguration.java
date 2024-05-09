package cz.muni.fi.obs.config;

import org.springframework.context.annotation.Bean;

import cz.muni.fi.obs.http.CurrencyServiceClient;

public class FeignClientConfiguration {

	public static class CurrencyServiceClientConfiguration {

		@Bean
		public CurrencyServiceClient fallbackFactory() {
			return new CurrencyServiceClient.Fallback();
		}
	}
}
