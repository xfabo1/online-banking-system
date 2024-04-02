package cz.muni.fi.obs.service;

import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.data.repository.CurrencyRepository;
import cz.muni.fi.obs.dto.CurrencyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public Page<CurrencyDto> listPage(Pageable pageable) {
        final Page<Currency> currencyPagedResult = currencyRepository.listPage(pageable);
        List<CurrencyDto> results = currencyPagedResult.getContent().stream().map(currency ->
                new CurrencyDto(currency.getName(), currency.getCode())).toList();

        return new PageImpl<>(results, pageable, currencyPagedResult.getTotalElements());
    }
}
