package cz.muni.fi.obs.repository;

import cz.muni.fi.obs.data.RepositoryDataProvider;
import cz.muni.fi.obs.domain.Currency;
import cz.muni.fi.obs.dto.PageRequest;
import cz.muni.fi.obs.dto.PagedResult;
import cz.muni.fi.obs.exception.MissingObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {CurrencyRepository.class, RepositoryDataProvider.class})
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    void findByCode_whenPresent_returnsCurrency() {
        Currency currency = currencyRepository.findByCode("usd");
        assertEquals("usd", currency.getCode());
    }

    @Test
    void findByCode_whenMissing_throwsMissingObject() {
        assertThrows(MissingObject.class, () -> currencyRepository.findByCode("aud"));
    }

    @Test
    void listFirstPage_whenThreePresent_returnsThree() {
        PageRequest pageRequest = new PageRequest(0, 10);
        PagedResult<Currency> currencyPagedResult = currencyRepository.listPage(pageRequest);

        assertEquals(3, currencyPagedResult.count());
        assertEquals(pageRequest, currencyPagedResult.request());
        assertEquals(3, currencyPagedResult.result().size());
    }

    @Test
    void listSmallPage_whenThreePresent_returnsLess() {
        PageRequest pageRequest = new PageRequest(0, 2);
        PagedResult<Currency> currencyPagedResult = currencyRepository.listPage(pageRequest);

        assertEquals(3, currencyPagedResult.count());
        assertEquals(pageRequest, currencyPagedResult.request());
        assertEquals(2, currencyPagedResult.result().size());
    }

    @Test
    void listNextPage_whenThreePresent_returnsNothing() {
        PageRequest pageRequest = new PageRequest(2, 2);
        PagedResult<Currency> currencyPagedResult = currencyRepository.listPage(pageRequest);

        assertEquals(3, currencyPagedResult.count());
        assertEquals(pageRequest, currencyPagedResult.request());
        assertEquals(0, currencyPagedResult.result().size());
    }
}