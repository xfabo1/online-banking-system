package cz.muni.fi.obs.service;

import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.data.dbo.ExchangeRate;
import cz.muni.fi.obs.data.provider.RepositoryDataProvider;
import cz.muni.fi.obs.data.repository.CurrencyRepository;
import cz.muni.fi.obs.data.repository.ExchangeRateRepository;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.exception.MissingObject;
import cz.muni.fi.obs.exception.NoExchangeRate;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {RepositoryDataProvider.class})
class ExchangeRateServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Autowired
    private Currency euro;

    @Autowired
    private Currency usd;

    @Autowired
    private Currency yuan;

    @Test
    void givenTwoCurrencies_currenciesCanBeExchanged_calculatesExchangeRate() {
        ExchangeRate exchangeRate = new ExchangeRate(
                Instant.now(),
                Instant.now().plusSeconds(300),
                usd,
                euro,
                1.2D
        );
        when(currencyRepository.findByCode("eur")).thenReturn(Optional.ofNullable((euro)));
        when(currencyRepository.findByCode("usd")).thenReturn(Optional.ofNullable(usd));
        when(exchangeRateRepository.findCurrentExchangeRate(usd.getId(), euro.getId())).thenReturn(Optional.of(exchangeRate));

        CurrencyExchangeResult result = exchangeRateService.exchange("usd",
                "eur",
                BigDecimal.valueOf(1000));

        assertEquals(1.2, result.exchangeRate());
        assertEquals(result.sourceAmount(), BigDecimal.valueOf(1000));
        assertEquals(0, BigDecimal.valueOf(1200L).compareTo(result.destAmount()));
        assertEquals(result.symbolFrom(), "usd");
        assertEquals(result.symbolTo(), "eur");
    }

    @Test
    void givenTwoCurrencies_currenciesCantBeExchanged_throwException() {
        when(currencyRepository.findByCode("yuan")).thenReturn(Optional.ofNullable(yuan));
        when(currencyRepository.findByCode("usd")).thenReturn(Optional.ofNullable(usd));
        when(exchangeRateRepository.findCurrentExchangeRate(yuan.getId(), usd.getId())).thenReturn(Optional.empty());
        when(exchangeRateRepository.findCurrentExchangeRate(usd.getId(), yuan.getId())).thenReturn(Optional.empty());


        assertThrows( NoExchangeRate.class, () -> exchangeRateService.exchange("usd",
                "yuan",
                BigDecimal.valueOf(1000)));
    }
    
    @Test
    void givenTwoCurrencies_currenciesDontExist_throwException() {
        when(currencyRepository.findByCode("yuan")).thenReturn(Optional.ofNullable(euro));
        when(currencyRepository.findByCode("eur")).thenReturn(Optional.empty());

        assertThrows( MissingObject.class, () -> exchangeRateService.exchange("usd",
                "eur",
                BigDecimal.valueOf(1000)));
    }
}