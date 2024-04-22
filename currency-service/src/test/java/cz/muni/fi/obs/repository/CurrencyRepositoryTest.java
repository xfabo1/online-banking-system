package cz.muni.fi.obs.repository;

import cz.muni.fi.obs.common.PostgresqlTest;
import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.data.repository.CurrencyRepository;
import cz.muni.fi.obs.data.repository.ExchangeRateRepository;
import cz.muni.fi.obs.exception.MissingObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static cz.muni.fi.obs.config.RepositoryDataProvider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
class CurrencyRepositoryTest extends PostgresqlTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    private final List<Currency> initialData = List.of(usd(), euro(), yuan());

    @BeforeEach
    public void setUp() {
        exchangeRateRepository.saveAll(fromUsdExchangeRates);
        exchangeRateRepository.saveAll(fromYuanExchangeRates);
        exchangeRateRepository.saveAll(fromEurExchangeRates);
        exchangeRateRepository.saveAll(toEurExchangeRates);
        exchangeRateRepository.saveAll(toYuanExchangeRates);
        exchangeRateRepository.saveAll(toUsdExchangeRates);
        currencyRepository.saveAll(initialData);
    }

    @AfterEach
    public void tearDown() {
        currencyRepository.deleteAll();
    }


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
        Page<Currency> currencyPagedResult = currencyRepository.findAll(pageRequest);

        assertEquals(3, currencyPagedResult.getTotalElements());
        assertEquals(pageRequest, currencyPagedResult.getPageable());
        assertEquals(3, currencyPagedResult.getContent().size());
    }

    @Test
    void listSmallPage_whenThreePresent_returnsLess() {
        Pageable pageRequest = Pageable.ofSize(2);
        Page<Currency> currencyPagedResult = currencyRepository.findAll(pageRequest);

        assertEquals(3, currencyPagedResult.getTotalElements());
        assertEquals(pageRequest, currencyPagedResult.getPageable());
        assertEquals(2, currencyPagedResult.getContent().size());
    }

    @Test
    void listNextPage_whenThreePresent_returnsNothing() {
        Pageable pageRequest = Pageable.ofSize(2).withPage(2);
        Page<Currency> currencyPagedResult = currencyRepository.findAll(pageRequest);

        assertEquals(3, currencyPagedResult.getTotalElements());
        assertEquals(pageRequest, currencyPagedResult.getPageable());
        assertEquals(0, currencyPagedResult.getContent().size());
    }
}