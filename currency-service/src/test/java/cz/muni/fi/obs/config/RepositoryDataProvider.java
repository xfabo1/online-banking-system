package cz.muni.fi.obs.config;

import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.data.dbo.ExchangeRate;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class RepositoryDataProvider {

    private static final Currency usd = new Currency("usd", "US Dollar");

    private static final Currency euro = new Currency("eur", "Euro");

    private static final Currency yuan = new Currency("yuan", "Chinese Yuan");

    public static final Set<ExchangeRate> usdExchangeRates = new HashSet<>();

    public static final Set<ExchangeRate> euroExchangeRates = new HashSet<>();

    public static final Set<ExchangeRate> yuanExchangeRates = new HashSet<>();

    static {
        Instant now = Instant.now();

        ExchangeRate expiredExchangeRateUSDToEuro = ExchangeRate.builder()
                .conversionRate(1.1)
                .createdAt(now.minusSeconds(600)) // Expired
                .validUntil(now.minusSeconds(300)) // Expired
                .build();

        ExchangeRate currentExchangeRateUSDToEuro = ExchangeRate.builder()
                .conversionRate(1.2)
                .createdAt(now.minusSeconds(200))
                .validUntil(now.plusSeconds(200)) // Current
                .build();

        ExchangeRate expiredExchangeRateUSDToYuan = ExchangeRate.builder()
                .conversionRate(100)
                .createdAt(now.minusSeconds(300)) // Expired
                .validUntil(now.minusSeconds(120)) // Expired
                .build();

        usdExchangeRates.addAll(Set.of(expiredExchangeRateUSDToEuro, currentExchangeRateUSDToEuro, expiredExchangeRateUSDToYuan));

        ExchangeRate expiredExchangeRateEuroToUSD = ExchangeRate.builder()
                .conversionRate(0.909091) // EUR to USD
                .createdAt(now.minusSeconds(600)) // Expired
                .validUntil(now.minusSeconds(300)) // Expired
                .build();

        ExchangeRate expiredExchangeRateEuroToUSD2 = ExchangeRate.builder()
                .conversionRate(0.909091) // EUR to USD (Same conversion rate)
                .createdAt(now.minusSeconds(300)) // Expired
                .validUntil(now.minusSeconds(150)) // Expired
                .build();

        ExchangeRate currentExchangeRateEuroToUSD = ExchangeRate.builder()
                .conversionRate(0.95) // EUR to USD (Different conversion rate)
                .createdAt(now.minusSeconds(200))
                .validUntil(now.plusSeconds(200)) // Current
                .build();

        ExchangeRate expiredExchangeRateEuroToYuan = ExchangeRate.builder()
                .conversionRate(7.272727) // EUR to Chinese Yuan
                .createdAt(now.minusSeconds(300)) // Expired
                .validUntil(now.minusSeconds(120)) // Expired
                .build();

        euroExchangeRates.addAll(Set.of(expiredExchangeRateEuroToUSD, expiredExchangeRateEuroToUSD2, currentExchangeRateEuroToUSD, expiredExchangeRateEuroToYuan));

        ExchangeRate expiredExchangeRateYuanToUSD = ExchangeRate.builder()
                .conversionRate(0.1375) // Chinese Yuan to USD
                .createdAt(now.minusSeconds(300)) // Expired
                .validUntil(now.minusSeconds(150)) // Expired
                .build();

        ExchangeRate expiredExchangeRateYuanToUSD2 = ExchangeRate.builder()
                .conversionRate(0.1375) // Chinese Yuan to USD (Same conversion rate)
                .createdAt(now.minusSeconds(150)) // Expired
                .validUntil(now.minusSeconds(120)) // Expired
                .build();

        ExchangeRate expiredExchangeRateYuanToEuro = ExchangeRate.builder()
                .conversionRate(0.1375) // Chinese Yuan to EUR
                .createdAt(now.minusSeconds(300)) // Expired
                .validUntil(now.minusSeconds(120)) // Expired
                .build();

        yuanExchangeRates.addAll(Set.of(expiredExchangeRateYuanToUSD, expiredExchangeRateYuanToUSD2, expiredExchangeRateYuanToEuro));
    }

    public static Currency usd() {
        usd.getExchangeRatesFrom().addAll(usdExchangeRates);
        return usd;
    }

    public static Currency euro() {
        euro.getExchangeRatesFrom().addAll(euroExchangeRates);
        return euro;
    }

    public static Currency yuan() {
        yuan.getExchangeRatesFrom().addAll(yuanExchangeRates);
        return yuan;
    }
}
