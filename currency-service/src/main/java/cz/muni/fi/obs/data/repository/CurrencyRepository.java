package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.dto.PageRequest;
import cz.muni.fi.obs.dto.PagedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CurrencyRepository {

    private final List<Currency> data;

    @Autowired
    public CurrencyRepository(List<Currency> data) {
        this.data = data;
    }

    public Currency save(Currency currency) {
        int index = data.indexOf(currency);
        if (index == -1) {
            data.add(currency);
        }
        else {
            data.set(index, currency);
        }
        return currency;
    }

    public Optional<Currency> findByCode(String code) {
        return data.stream().filter(currency -> currency.getCode().equals(code))
                .findFirst();
    }

    public PagedResult<Currency> listPage(PageRequest pageRequest) {
        List<Currency> results = data.stream()
                .skip((long) pageRequest.page() * pageRequest.pageSize())
                .limit(pageRequest.pageSize()).toList();

        long count = data.size();

        return new PagedResult<>(results, count, pageRequest);
    }
}
