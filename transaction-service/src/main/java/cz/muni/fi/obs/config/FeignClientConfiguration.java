package cz.muni.fi.obs.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.muni.fi.obs.http.CurrencyServiceClient;

public class FeignClientConfiguration {

	@Configuration
	@EnableFeignClients(basePackages = "cz.muni.fi.obs.http")
	public static class FeignConfiguration {
	}

	public static class CurrencyServiceClientConfiguration {

		@Bean
		public CurrencyServiceClient fallbackFactory() {
			return new CurrencyServiceClient.Fallback();
		}
	}
}
