package cz.muni.fi.obs.config;

import cz.muni.fi.obs.http.TransactionServiceClient;
import cz.muni.fi.obs.http.TransactionServiceErrorDecoder;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

    public static class TransactionServiceClientConfiguration {

        @Bean
        public TransactionServiceClient fallbackFactory() {
            return new TransactionServiceClient.Fallback();
        }

        @Bean
        public TransactionServiceErrorDecoder transactionServiceErrorDecoder() {
            return new TransactionServiceErrorDecoder();
        }

    }
}
