package cz.muni.fi.obs.common;


import cz.muni.fi.obs.Application;
import cz.muni.fi.obs.config.JpaRepositoryConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = Application.class)
@ContextConfiguration(initializers = {PostgresqlTest.Initializer.class}, classes = {Application.class, JpaRepositoryConfig.class})
@Testcontainers
public abstract class PostgresqlTest {

    @Container
    protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("currency_db")
            .withUsername("currency_service")
            .withPassword("changemelater");

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
