package cz.muni.fi.obs.data;

import cz.muni.fi.obs.domain.Currency;
import cz.muni.fi.obs.domain.ExchangeRate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.Set;

@Configuration
public class RepositoryDataProvider {

    private final Currency usd = new Currency("usd", "US Dollar");

    private final Currency euro = new Currency("eur", "Euro");

    private final Currency yuan = new Currency("yuan", "Chinese Yuan");

    @Bean
    public Currency usd() {
        Instant now = Instant.now();

        ExchangeRate expiredExchangeRate = ExchangeRate.builder()
                .conversionRate(1.1)
                .from(usd)
                .to(euro)
                .createdAt(now.minusSeconds(600)) // Expired
                .validUntil(now.minusSeconds(300)) // Expired
                .build();

        ExchangeRate currentExchangeRate = ExchangeRate.builder()
                .conversionRate(1.2)
                .from(usd)
                .to(euro)
                .createdAt(now.minusSeconds(200))
                .validUntil(now.plusSeconds(200)) // Current
                .build();

        ExchangeRate expiredExchangeRate1 = ExchangeRate.builder()
                .conversionRate(100)
                .from(usd)
                .to(yuan)
                .createdAt(now.minusSeconds(300)) // Expired
                .validUntil(now.minusSeconds(120)) // Expired
                .build();

        usd.getExchangeRates().addAll(Set.of(expiredExchangeRate, currentExchangeRate, expiredExchangeRate1));

        return usd;
    }

    @Bean
    public Currency euro() {
        Instant now = Instant.now();

        ExchangeRate expiredExchangeRate = ExchangeRate.builder()
                .conversionRate(0.909091) // EUR to USD
                .from(euro)
                .to(usd)
                .createdAt(now.minusSeconds(600)) // Expired
                .validUntil(now.minusSeconds(300)) // Expired
                .build();

        ExchangeRate expiredExchangeRate2 = ExchangeRate.builder()
                .conversionRate(0.909091) // EUR to USD (Same conversion rate)
                .from(euro)
                .to(usd)
                .createdAt(now.minusSeconds(300)) // Expired
                .validUntil(now.minusSeconds(150)) // Expired
                .build();

        ExchangeRate currentExchangeRate = ExchangeRate.builder()
                .conversionRate(0.95) // EUR to USD (Different conversion rate)
                .from(euro)
                .to(usd)
                .createdAt(now.minusSeconds(200))
                .validUntil(now.plusSeconds(200)) // Current
                .build();

        ExchangeRate expiredExchangeRate3 = ExchangeRate.builder()
                .conversionRate(7.272727) // EUR to Chinese Yuan
                .from(euro)
                .to(yuan)
                .createdAt(now.minusSeconds(300)) // Expired
                .validUntil(now.minusSeconds(120)) // Expired
                .build();

        euro.getExchangeRates().addAll(Set.of(expiredExchangeRate, expiredExchangeRate2, currentExchangeRate, expiredExchangeRate3));

        return euro;
    }

    @Bean
    public Currency yuan() {
        Instant now = Instant.now();

        ExchangeRate expiredExchangeRate = ExchangeRate.builder()
                .conversionRate(0.1375) // Chinese Yuan to USD
                .from(yuan)
                .to(usd)
                .createdAt(now.minusSeconds(300)) // Expired
                .validUntil(now.minusSeconds(150)) // Expired
                .build();

        ExchangeRate expiredExchangeRate2 = ExchangeRate.builder()
                .conversionRate(0.1375) // Chinese Yuan to USD (Same conversion rate)
                .from(yuan)
                .to(usd)
                .createdAt(now.minusSeconds(150)) // Expired
                .validUntil(now.minusSeconds(120)) // Expired
                .build();

        ExchangeRate expiredExchangeRate3 = ExchangeRate.builder()
                .conversionRate(0.1375) // Chinese Yuan to EUR
                .from(yuan)
                .to(euro)
                .createdAt(now.minusSeconds(300)) // Expired
                .validUntil(now.minusSeconds(120)) // Expired
                .build();

        yuan.getExchangeRates().addAll(Set.of(expiredExchangeRate, expiredExchangeRate2, expiredExchangeRate3));

        return yuan;
    }
}
