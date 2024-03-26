package cz.muni.fi.obs.repository;

import cz.muni.fi.obs.domain.Currency;
import cz.muni.fi.obs.domain.ExchangeRate;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.Optional;

@Repository
public class ExchangeRateRepository {

    // fixme: temporary way of finding exchange rate
    public Optional<ExchangeRate> findLatestExchangeRate(Currency from, Currency to) {
        return from.getExchangeRates().stream().filter(rate -> rate.getFrom().equals(from) && rate.getTo().equals(to))
                .min(Comparator.comparing(ExchangeRate::getValidUntil)).stream()
                .findFirst();
    }
}
