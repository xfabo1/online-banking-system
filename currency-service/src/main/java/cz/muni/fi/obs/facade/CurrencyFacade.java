package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.dto.CurrencyDto;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.service.CurrencyService;
import cz.muni.fi.obs.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    public Page<CurrencyDto> listPage(Pageable pageRequest) {
        Page<Currency> currencies = currencyService.listPage(pageRequest);
        List<CurrencyDto> dtos = currencies.getContent().stream()
                .map(currency -> new CurrencyDto(currency.getName(), currency.getCode()))
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageRequest, currencies.getTotalElements());
    }

    public boolean codeExists(String code) {
        return currencyService.codeExists(code);
    }
}
