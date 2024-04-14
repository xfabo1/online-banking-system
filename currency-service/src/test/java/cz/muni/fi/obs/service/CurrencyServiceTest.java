package cz.muni.fi.obs.service;

import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.data.provider.RepositoryDataProvider;
import cz.muni.fi.obs.data.repository.CurrencyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {RepositoryDataProvider.class})
class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    @Autowired
    private List<Currency> mockData;

    @Test
    public void listPage_whenThreeCurrenciesPresent_returnsThreeCurrencies() {
        Pageable pageable = Pageable.ofSize(10);
        when(currencyRepository.findAllPaged(pageable)).thenReturn(new PageImpl<>(mockData, pageable, 3));

        Page<Currency> currencies = currencyService.listPage(pageable);

        Assertions.assertThat(currencies.getTotalElements()).isEqualTo(3);
    }
}