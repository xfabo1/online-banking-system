package cz.muni.fi.obs.service;

import cz.muni.fi.obs.common.PostgresqlTest;
import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.data.repository.CurrencyRepository;
import cz.muni.fi.obs.data.repository.ExchangeRateRepository;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.exception.MissingObject;
import cz.muni.fi.obs.exception.NoExchangeRate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static cz.muni.fi.obs.config.RepositoryDataProvider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
class ExchangeRateServiceIntegrationTest extends PostgresqlTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;

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
    void givenTwoCurrencies_currenciesCanBeExchanged_calculatesExchangeRate() {
        CurrencyExchangeResult result = exchangeRateService.exchange("usd", "eur", BigDecimal.valueOf(1000));

        assertEquals(1.2, result.exchangeRate());
        assertEquals(0, BigDecimal.valueOf(1200).compareTo(result.destAmount()));
        assertEquals(result.symbolFrom(), "usd");
        assertEquals(result.symbolTo(), "eur");
    }

    @Test
    void givenTwoCurrencies_currenciesCantBeExchanged_throwException() {
        assertThrows(NoExchangeRate.class, () -> exchangeRateService.exchange("usd",
                "yuan",
                BigDecimal.valueOf(1000)));
    }

    @Test
    void givenTwoCurrencies_currenciesDontExist_throwException() {
        assertThrows(MissingObject.class, () -> exchangeRateService.exchange("usd",
                "bla",
                BigDecimal.valueOf(1000)));
    }
}