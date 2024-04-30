package cz.muni.fi.obs.http;

import cz.muni.fi.obs.api.CurrencyExchangeRequest;
import cz.muni.fi.obs.api.CurrencyExchangeResult;
import cz.muni.fi.obs.config.FeignClientConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@FeignClient(
        name = "currency-service",
        url = "${clients.currency-service.url}",
        configuration = FeignClientConfiguration.CurrencyServiceClientConfiguration.class,
        fallback = CurrencyServiceClient.Fallback.class
)
public interface CurrencyServiceClient {

    @PostMapping("/v1/currencies/exchange")
    Optional<CurrencyExchangeResult> getCurrencyExchange(CurrencyExchangeRequest currencyExchangeRequest);

    @Slf4j
    class Fallback implements CurrencyServiceClient {

        @Override
        public Optional<CurrencyExchangeResult> getCurrencyExchange(CurrencyExchangeRequest currencyExchangeRequest) {
            log.warn("Could not get currency exchange rate, returning null");
            return Optional.empty();
        }
    }
}
