package cz.muni.fi.obs.service;

import cz.muni.fi.obs.domain.Currency;
import cz.muni.fi.obs.domain.ExchangeRate;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.exception.NoExchangeRate;
import cz.muni.fi.obs.repository.CurrencyRepository;
import cz.muni.fi.obs.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ExchangeRateService {

    private final CurrencyRepository currencyRepository;

    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateService(CurrencyRepository currencyRepository, ExchangeRateRepository exchangeRateRepository) {
        this.currencyRepository = currencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    /**
     * Method for exchange in between two currencies
     *
     * @param codeFrom of source currency
     * @param codeTo of destination currency
     * @param amount to exchange
     * @return result of exchange
     */
    public CurrencyExchangeResult exchange(String codeFrom, String codeTo, BigDecimal amount) {
        final Currency from = currencyRepository.findByCode(codeFrom);
        final Currency to = currencyRepository.findByCode(codeTo);
        final Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findCurrentExchangeRate(from, to);
        final Optional<ExchangeRate> inverseExchangeRate = exchangeRateRepository.findCurrentExchangeRate(to, from);

        CurrencyExchangeResult currencyExchangeResult = null;

        if (exchangeRate.isPresent()) {
            currencyExchangeResult = createResult(from, to, exchangeRate.get().getConversionRate(), amount);
        } else if (inverseExchangeRate.isPresent()) {
            currencyExchangeResult = createResult(from, to, 1 / inverseExchangeRate.get().getConversionRate(), amount);
        }

        if (currencyExchangeResult != null) {
            return currencyExchangeResult;
        }
        else {
            throw new NoExchangeRate(from, to);
        }
    }

    private CurrencyExchangeResult createResult(Currency from, Currency to, Double conversionRate, BigDecimal amount) {
        return new CurrencyExchangeResult(from.getCode(),
                to.getCode(),
                conversionRate,
                amount,
                amount.multiply(BigDecimal.valueOf(conversionRate)));
    }

}
