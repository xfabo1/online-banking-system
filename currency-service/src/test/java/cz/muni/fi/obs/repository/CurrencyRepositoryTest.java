package cz.muni.fi.obs.repository;

import cz.muni.fi.obs.Application;
import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.data.repository.CurrencyRepository;
import cz.muni.fi.obs.exception.MissingObject;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration(initializers = {CurrencyRepositoryTest.Initializer.class})
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private List<Currency> initialData;

    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.5")
            .withDatabaseName("currency_db")
            .withUsername("currency_service")
            .withPassword("changemelater");

    @BeforeEach
    public void setUp() {
        currencyRepository.saveAll(initialData);
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

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}