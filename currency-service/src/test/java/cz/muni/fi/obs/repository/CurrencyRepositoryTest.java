package cz.muni.fi.obs.repository;

import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.data.provider.RepositoryDataProvider;
import cz.muni.fi.obs.data.repository.CurrencyRepository;
import cz.muni.fi.obs.exception.MissingObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {CurrencyRepository.class, RepositoryDataProvider.class})
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    void findByCode_whenPresent_returnsCurrency() {
        Currency currency = currencyRepository.findByCode("usd").orElseThrow(() -> new MissingObject(Currency.class, "usd"));
        assertEquals("usd", currency.getCode());
    }

    @Test
    void findByCode_whenMissing_throwsMissingObject() {
        assertTrue(currencyRepository.findByCode("aud").isEmpty());
    }

    @Test
    void listFirstPage_whenThreePresent_returnsThree() {
        Pageable pageRequest = Pageable.ofSize(10);
        Page<Currency> currencyPagedResult = currencyRepository.findAllPaged(pageRequest);

        assertEquals(3, currencyPagedResult.getTotalElements());
        assertEquals(pageRequest, currencyPagedResult.getPageable());
        assertEquals(3, currencyPagedResult.getContent().size());
    }

    @Test
    void listSmallPage_whenThreePresent_returnsLess() {
        Pageable pageRequest = Pageable.ofSize(2);
        Page<Currency> currencyPagedResult = currencyRepository.findAllPaged(pageRequest);

        assertEquals(3, currencyPagedResult.getTotalElements());
        assertEquals(pageRequest, currencyPagedResult.getPageable());
        assertEquals(2, currencyPagedResult.getContent().size());
    }

    @Test
    void listNextPage_whenThreePresent_returnsNothing() {
        Pageable pageRequest = Pageable.ofSize(2).withPage(2);
        Page<Currency> currencyPagedResult = currencyRepository.findAllPaged(pageRequest);

        assertEquals(3, currencyPagedResult.getTotalElements());
        assertEquals(pageRequest, currencyPagedResult.getPageable());
        assertEquals(0, currencyPagedResult.getContent().size());
    }
}