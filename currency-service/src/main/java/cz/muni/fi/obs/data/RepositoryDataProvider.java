package cz.muni.fi.obs.data;

import cz.muni.fi.obs.domain.Currency;
import cz.muni.fi.obs.domain.ExchangeRate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Configuration
public class RepositoryDataProvider {

    private final Currency currency1 = new Currency("usd", "US Dollar");

    private final Currency currency2 = new Currency("eur", "Euro");

    private final Currency currency3 = new Currency("yuan", "Chinese Yuan");

    @Bean
    public Currency currency1() {
        Instant now = Instant.now();

        ExchangeRate exchangeRate = ExchangeRate.builder()
                .conversionRate(1.1)
                .from(currency1)
                .to(currency2)
                .createdAt(now)
                .validUntil(now.plusSeconds(300))
                .build();

        ExchangeRate exchangeRate1 = ExchangeRate.builder()
                .conversionRate(100)
                .from(currency1)
                .to(currency3)
                .createdAt(now)
                .validUntil(now.plusSeconds(120))
                .build();

        currency1.setExchangeRates(Set.of(exchangeRate1, exchangeRate));

        return currency1;
    }

    @Bean
    public Currency currency2() {
        Instant now = Instant.now();
        Instant fiveMinutesLater = now.plusSeconds(300);

        ExchangeRate exchangeRate1 = ExchangeRate.builder()
                .conversionRate(0.909091) // EUR to USD
                .from(currency2)
                .to(currency1)
                .createdAt(now)
                .validUntil(fiveMinutesLater)
                .build();

        ExchangeRate exchangeRate2 = ExchangeRate.builder()
                .conversionRate(0.909091) // EUR to USD (Same conversion rate)
                .from(currency2)
                .to(currency1)
                .createdAt(fiveMinutesLater)
                .validUntil(fiveMinutesLater.plusSeconds(300)) // Different validity interval
                .build();

        ExchangeRate exchangeRate3 = ExchangeRate.builder()
                .conversionRate(7.272727) // EUR to Chinese Yuan
                .from(currency2)
                .to(currency3)
                .createdAt(now)
                .validUntil(now.plusSeconds(120))
                .build();

        currency2.setExchangeRates(Set.of(exchangeRate1, exchangeRate2, exchangeRate3));

        return currency2;
    }

    @Bean
    public Currency currency3() {
        Instant now = Instant.now();
        Instant twoMinutesLater = now.plusSeconds(120);

        ExchangeRate exchangeRate1 = ExchangeRate.builder()
                .conversionRate(0.1375) // Chinese Yuan to USD
                .from(currency3)
                .to(currency1)
                .createdAt(now)
                .validUntil(now.plusSeconds(300))
                .build();

        ExchangeRate exchangeRate2 = ExchangeRate.builder()
                .conversionRate(0.1375) // Chinese Yuan to USD (Same conversion rate)
                .from(currency3)
                .to(currency1)
                .createdAt(now.plusSeconds(300))
                .validUntil(twoMinutesLater) // Different validity interval
                .build();

        ExchangeRate exchangeRate3 = ExchangeRate.builder()
                .conversionRate(0.1375) // Chinese Yuan to EUR
                .from(currency3)
                .to(currency2)
                .createdAt(now)
                .validUntil(now.plusSeconds(120))
                .build();

        currency3.setExchangeRates(Set.of(exchangeRate1, exchangeRate2, exchangeRate3));

        return currency3;
    }
}
