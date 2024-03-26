package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyFacadeImpl implements CurrencyFacade {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public CurrencyFacadeImpl(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public CurrencyExchangeResult exchange(String currencyFrom, String currencyTo, BigDecimal amount) {
        return exchangeRateService.exchange(currencyFrom, currencyTo, amount);
    }
}
