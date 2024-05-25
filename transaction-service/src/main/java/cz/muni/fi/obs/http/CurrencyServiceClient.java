package cz.muni.fi.obs.http;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import cz.muni.fi.obs.api.CurrencyExchangeRequest;
import cz.muni.fi.obs.api.CurrencyExchangeResult;
import cz.muni.fi.obs.config.FeignClientConfiguration;
import lombok.extern.slf4j.Slf4j;

@FeignClient(
        name = "currency-service",
        url = "${clients.currency-service.url}",
        configuration = FeignClientConfiguration.CurrencyServiceClientConfiguration.class,
        fallback = CurrencyServiceClient.Fallback.class
)
public interface CurrencyServiceClient {

    @PostMapping("/v1/currencies/exchange")
    Optional<CurrencyExchangeResult> getCurrencyExchange(CurrencyExchangeRequest currencyExchangeRequest);

    @GetMapping("/v1/currencies/exists")
    boolean currencyExists(String currencyCode);

    @Slf4j
    class Fallback implements CurrencyServiceClient {

        @Override
        public Optional<CurrencyExchangeResult> getCurrencyExchange(CurrencyExchangeRequest currencyExchangeRequest) {
            log.warn("Could not get currency exchange rate, returning null");
            return Optional.empty();
        }

        @Override
        public boolean currencyExists(String currencyCode) {
            log.warn("Could not check if currency exists, returning false");
            return false;
        }
    }
}
