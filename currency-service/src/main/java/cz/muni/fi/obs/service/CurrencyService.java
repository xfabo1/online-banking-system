package cz.muni.fi.obs.service;

import cz.muni.fi.obs.domain.Currency;
import cz.muni.fi.obs.dto.CurrencyDto;
import cz.muni.fi.obs.dto.PageRequest;
import cz.muni.fi.obs.dto.PagedResult;
import cz.muni.fi.obs.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public PagedResult<CurrencyDto> listPage(PageRequest pageRequest) {
        final PagedResult<Currency> currencyPagedResult = currencyRepository.listPage(pageRequest);
        List<CurrencyDto> results = currencyPagedResult.result().stream().map(currency ->
                new CurrencyDto(currency.getName(), currency.getCode())).toList();

        return new PagedResult<>(results, currencyPagedResult.count(), pageRequest);
    }
}
