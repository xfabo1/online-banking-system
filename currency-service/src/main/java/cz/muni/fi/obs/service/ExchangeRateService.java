package cz.muni.fi.obs.service;

import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.data.dbo.ExchangeRate;
import cz.muni.fi.obs.data.repository.CurrencyRepository;
import cz.muni.fi.obs.data.repository.ExchangeRateRepository;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.exception.MissingObject;
import cz.muni.fi.obs.exception.NoExchangeRate;
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

    public Currency findByCode(String code) {
        return currencyRepository.findByCode(code).orElseThrow(() -> new MissingObject(Currency.class, code));
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
        final Currency from = findByCode(codeFrom);
        final Currency to = findByCode(codeTo);
        final Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findCurrentExchangeRate(from, to);
        final Optional<ExchangeRate> inverseExchangeRate = exchangeRateRepository.findCurrentExchangeRate(to, from);

        CurrencyExchangeResult currencyExchangeResult = null;

        if (exchangeRate.isPresent()) {
            currencyExchangeResult = createResult(from, to, exchangeRate.get().getConversionRate(), amount);
        } else if (inverseExchangeRate.isPresent()) {
            currencyExchangeResult = createResult(from, to, 1 / inverseExchangeRate.get().getConversionRate(), amount);
        }

        if (currencyExchangeResult == null) {
            throw new NoExchangeRate(from, to);
        }
        return currencyExchangeResult;
    }

    private CurrencyExchangeResult createResult(Currency from, Currency to, Double conversionRate, BigDecimal amount) {
        return new CurrencyExchangeResult(from.getCode(),
                to.getCode(),
                conversionRate,
                amount,
                amount.multiply(BigDecimal.valueOf(conversionRate)));
    }
}
