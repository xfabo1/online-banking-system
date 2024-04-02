package cz.muni.fi.obs.web;

import cz.muni.fi.obs.api.CurrencyExchangeResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CurrencyServiceClient {
    public CurrencyExchangeResult getConversionRate() {
        return CurrencyExchangeResult.builder()
                .sourceAmount(BigDecimal.valueOf(100))
                .exchangedAmount(BigDecimal.valueOf(4))
                .exchangedRate(BigDecimal.valueOf(4))
                .symbolFrom("CZK")
                .symbolTo("EUR").build();
    }
}
