package cz.muni.fi.obs.service;

import cz.muni.fi.obs.domain.Currency;
import cz.muni.fi.obs.domain.ExchangeRate;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.exception.MissingObject;
import cz.muni.fi.obs.exception.NoExchangeRate;
import cz.muni.fi.obs.repository.CurrencyRepository;
import cz.muni.fi.obs.repository.ExchangeRateRepository;
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

@SpringBootTest
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
        when(currencyRepository.findByCode("eur")).thenReturn(Optional.ofNullable((euro)));
        when(currencyRepository.findByCode("usd")).thenReturn(Optional.ofNullable(usd));
        when(exchangeRateRepository.findCurrentExchangeRate(usd, euro)).thenReturn(Optional.of(ExchangeRate
                .builder()
                .conversionRate(1.2)
                .from(usd)
                .to(euro)
                .createdAt(Instant.now())
                .validUntil(Instant.now().plusSeconds(100)).build()));

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
        when(exchangeRateRepository.findCurrentExchangeRate(yuan, usd)).thenReturn(Optional.empty());
        when(exchangeRateRepository.findCurrentExchangeRate(usd, yuan)).thenReturn(Optional.empty());


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