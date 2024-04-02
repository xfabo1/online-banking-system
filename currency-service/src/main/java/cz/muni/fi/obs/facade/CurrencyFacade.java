package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.dto.CurrencyDto;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.service.CurrencyService;
import cz.muni.fi.obs.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyFacade {

    private final CurrencyService currencyService;

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public CurrencyFacade(CurrencyService currencyService, ExchangeRateService exchangeRateService) {
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
    }

    public CurrencyExchangeResult exchange(String currencyFrom, String currencyTo, BigDecimal amount) {
        return exchangeRateService.exchange(currencyFrom, currencyTo, amount);
    }

    public Page<CurrencyDto> listPaged(Pageable pageRequest) {
        return currencyService.listPage(pageRequest);
    }
}
