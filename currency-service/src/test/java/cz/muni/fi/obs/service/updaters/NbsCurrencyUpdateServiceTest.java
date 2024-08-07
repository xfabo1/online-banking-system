package cz.muni.fi.obs.service.updaters;

import cz.muni.fi.obs.common.PostgresqlTest;
import cz.muni.fi.obs.data.repository.CurrencyRepository;
import cz.muni.fi.obs.service.update.NbsCurrencyUpdateService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {"currency.auto-update.services.nbs.url:https://nbs.sk/export/sk/exchange-rate/latest/csv"})
public class NbsCurrencyUpdateServiceTest extends PostgresqlTest {

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

        assertTrue(currencyRepository.findAll(Pageable.ofSize(100)).getTotalElements() > 3);
    }
}
