package cz.muni.fi.obs.service;

import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.data.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public Page<Currency> listPage(Pageable pageable) {
        return currencyRepository.findAllPaged(pageable);
    }
}
