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

    public static final Set<ExchangeRate> fromUsdExchangeRates = new HashSet<>();

    public static final Set<ExchangeRate> fromEurExchangeRates = new HashSet<>();

    public static final Set<ExchangeRate> fromYuanExchangeRates = new HashSet<>();

    public static final Set<ExchangeRate> toUsdExchangeRates = new HashSet<>();

    public static final Set<ExchangeRate> toEurExchangeRates = new HashSet<>();

    public static final Set<ExchangeRate> toYuanExchangeRates = new HashSet<>();


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

        fromUsdExchangeRates.addAll(Set.of(expiredExchangeRateUSDToEuro, currentExchangeRateUSDToEuro, expiredExchangeRateUSDToYuan));

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

        fromEurExchangeRates.addAll(Set.of(expiredExchangeRateEuroToUSD, expiredExchangeRateEuroToUSD2, currentExchangeRateEuroToUSD, expiredExchangeRateEuroToYuan));

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

        toUsdExchangeRates.addAll(Set.of(expiredExchangeRateEuroToUSD, expiredExchangeRateEuroToUSD2, currentExchangeRateEuroToUSD, expiredExchangeRateYuanToUSD,
                expiredExchangeRateYuanToUSD2));
        toEurExchangeRates.addAll(Set.of(expiredExchangeRateUSDToEuro, currentExchangeRateUSDToEuro, expiredExchangeRateYuanToEuro));
        toYuanExchangeRates.addAll(Set.of(expiredExchangeRateEuroToYuan, expiredExchangeRateUSDToYuan));

        fromYuanExchangeRates.addAll(Set.of(expiredExchangeRateYuanToUSD, expiredExchangeRateYuanToUSD2, expiredExchangeRateYuanToEuro));
    }

    public static Currency usd() {
        usd.getExchangeRatesTo().addAll(toUsdExchangeRates);
        usd.getExchangeRatesFrom().addAll(fromUsdExchangeRates);
        return usd;
    }

    public static Currency euro() {
        euro.getExchangeRatesTo().addAll(toEurExchangeRates);
        euro.getExchangeRatesFrom().addAll(fromEurExchangeRates);
        return euro;
    }

    public static Currency yuan() {
        yuan.getExchangeRatesTo().addAll(toYuanExchangeRates);
        yuan.getExchangeRatesFrom().addAll(fromYuanExchangeRates);
        return yuan;
    }
}
