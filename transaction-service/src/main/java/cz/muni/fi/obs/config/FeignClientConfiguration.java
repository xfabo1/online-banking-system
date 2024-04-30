package cz.muni.fi.obs.config;

import cz.muni.fi.obs.http.CurrencyServiceClient;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

	public static class CurrencyServiceClientConfiguration {

		@Bean
		public CurrencyServiceClient fallbackFactory() {
			return new CurrencyServiceClient.Fallback();
		}
	}
}
