package cz.muni.fi.obs.service;

import cz.muni.fi.obs.data.repository.CurrencyRepository;
import cz.muni.fi.obs.dto.PageRequest;
import cz.muni.fi.obs.service.update.NbsCurrencyUpdateService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {"currency.auto-update.urls.nbs:https://nbs.sk/export/sk/exchange-rate/latest/csv"})
public class NbsCurrencyUpdateServiceTest {

    @Autowired
    private MockWebServer mockServer;

    @Autowired
    private NbsCurrencyUpdateService nbsCurrencyUpdateService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    void autoUpdate_validResponse_updatesCurrencies() {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(CurrencySheetsResponse.nbsResponse));

        nbsCurrencyUpdateService.updateCurrencies();

        assertTrue(currencyRepository.listPage(new PageRequest(0, 100)).count() > 3);
    }
}
