package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.data.dbo.ExchangeRate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Comparator;
import java.util.Optional;

@Repository
public class ExchangeRateRepository {

    // fixme: temporary way of finding exchange rate
    public Optional<ExchangeRate> findCurrentExchangeRate(Currency from, Currency to) {
        return from.getExchangeRates().stream().filter(rate -> rate.getFrom().equals(from) && rate.getTo().equals(to))
                .filter(rate -> {
                    Instant now = Instant.now();
                    return rate.getCreatedAt().isBefore(now) && rate.getValidUntil().isAfter(now);
                })
                .min(Comparator.comparing(ExchangeRate::getValidUntil)).stream()
                .findFirst();
    }
}
