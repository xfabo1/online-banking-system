package cz.muni.fi.obs;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.util.UriComponents;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("postgres")
@DirtiesContext
@ContextConfiguration(initializers = {ControllerIntegrationTest.Initializer.class}, classes = {TransactionManagement.class})
public abstract class ControllerIntegrationTest {

    private static final String ACTIVEMQ_IMAGE = "apache/activemq-classic:6.1.0";
    private static final int ACTIVEMQ_PORT = 61616;

    @Container
    public static final ActiveMQContainer activeMqContainer = new ActiveMQContainer(ACTIVEMQ_IMAGE)
            .withUser("username")
            .withPassword("password")
            .withExposedPorts(ACTIVEMQ_PORT);

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("currency_db")
            .withUsername("currency_service")
            .withPassword("changemelater");


    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }


    public static RequestSpecification requestSpecification(UriComponents uri) {
        return given().basePath(uri.getPath())
                .queryParams(uri.getQueryParams());
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                            "spring.activemq.broker-url=" + activeMqContainer.getBrokerUrl(),
                            "spring.activemq.user=" + activeMqContainer.getUser(),
                            "spring.activemq.password=" + activeMqContainer.getPassword())
                    .applyTo(applicationContext.getEnvironment());
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}
