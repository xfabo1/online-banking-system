package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.config.RepositoryDataProvider;
import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.dto.CurrencyDto;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.service.CurrencyService;
import cz.muni.fi.obs.service.ExchangeRateService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static cz.muni.fi.obs.config.RepositoryDataProvider.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {RepositoryDataProvider.class})
class CurrencyFacadeTest {

    @Mock
    private CurrencyService currencyService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private CurrencyFacade currencyFacade;

    private List<Currency> testData = List.of(usd(), euro(), yuan());
    ;

    @Test
    void exchange_betweenTwoCurrencies_returnsResult() {
        String code = testData.get(1).getCode();
        String code1 = testData.getFirst().getCode();
        when(exchangeRateService.exchange(any(String.class), any(String.class), any(BigDecimal.class)))
                .thenReturn(new CurrencyExchangeResult(code1, code, 10.2, BigDecimal.valueOf(1000), BigDecimal.valueOf(10200)));

        CurrencyExchangeResult exchange = currencyFacade.exchange(code, code1, BigDecimal.valueOf(1000));

        Assertions.assertThat(exchange.destAmount()).isEqualTo(BigDecimal.valueOf(10200));
    }

    @Test
    void listPage_threeCurrenciesExist_returnsThreeCurrencies() {
        Pageable pageable = Pageable.ofSize(10);
        when(currencyService.listPage(pageable)).thenReturn(new PageImpl<>(testData, pageable, 3));

        Page<CurrencyDto> currencies = currencyFacade.listPage(pageable);

        Assertions.assertThat(currencies.getTotalElements()).isEqualTo(3);
    }
}