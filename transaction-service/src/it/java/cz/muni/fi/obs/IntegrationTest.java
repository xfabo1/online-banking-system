package cz.muni.fi.obs;

import static io.restassured.RestAssured.given;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.util.UriComponents;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import cz.muni.fi.obs.jms.JmsProducer;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("postgres")
@DirtiesContext
@ContextConfiguration(initializers = {IntegrationTest.Initializer.class}, classes = {TransactionManagement.class})
public abstract class IntegrationTest {

    @Autowired
    protected JmsTemplate jmsTemplate;

    private static final String ACTIVEMQ_IMAGE = "apache/activemq-classic:6.1.0";
    private static final String POSTGRES_IMAGE = "postgres:16.2";
    private static final int ACTIVEMQ_PORT = 61616;

    @Container
    public static final ActiveMQContainer activeMqContainer = new ActiveMQContainer(ACTIVEMQ_IMAGE)
            .withUser("username")
            .withPassword("password")
            .withExposedPorts(ACTIVEMQ_PORT);

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
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

    protected void waitForQueue() throws InterruptedException {
        Integer queueSize = 1;

        while (queueSize > 0) {
            queueSize = jmsTemplate.browse(JmsProducer.TRANSACTION_QUEUE_NAME, (session, browser) -> Collections.list(browser.getEnumeration()).size());
            Thread.sleep(1000);
        }
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
