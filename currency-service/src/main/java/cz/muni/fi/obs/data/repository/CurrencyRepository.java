package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public Page<Currency> listPage(Pageable pageable) {
        List<Currency> results = data.stream()
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize()).toList();

        long count = data.size();

        return new PageImpl<>(results, pageable, count);
    }
}
