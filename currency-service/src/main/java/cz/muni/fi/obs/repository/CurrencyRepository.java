package cz.muni.fi.obs.repository;

import cz.muni.fi.obs.domain.Currency;
import cz.muni.fi.obs.dto.PageRequest;
import cz.muni.fi.obs.dto.PagedResult;
import cz.muni.fi.obs.exception.MissingObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CurrencyRepository {

    private final List<Currency> data;

    @Autowired
    public CurrencyRepository(List<Currency> data) {
        this.data = data;
    }

    public Currency findByCode(String code) {
        return data.stream().filter(currency -> currency.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new MissingObject(Currency.class, code));
    }

    public PagedResult<Currency> listPage(PageRequest pageRequest) {
        List<Currency> results = data.stream()
                .skip((long) pageRequest.page() * pageRequest.pageSize())
                .limit(pageRequest.pageSize()).toList();

        long count = data.size();

        return new PagedResult<>(results, count, pageRequest);
    }
}
