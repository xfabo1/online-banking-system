package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.dto.CurrencyDto;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.dto.PageRequest;
import cz.muni.fi.obs.dto.PagedResult;
import cz.muni.fi.obs.service.CurrencyService;
import cz.muni.fi.obs.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyFacadeImpl implements CurrencyFacade {

    private final CurrencyService currencyService;

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public CurrencyFacadeImpl(CurrencyService currencyService, ExchangeRateService exchangeRateService) {
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public CurrencyExchangeResult exchange(String currencyFrom, String currencyTo, BigDecimal amount) {
        return exchangeRateService.exchange(currencyFrom, currencyTo, amount);
    }

    @Override
    public PagedResult<CurrencyDto> listPaged(PageRequest pageRequest) {
        return currencyService.listPage(pageRequest);
    }
}
