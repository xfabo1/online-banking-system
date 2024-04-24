package cz.muni.fi.obs.integration;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponents;

import static io.restassured.RestAssured.given;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/seed_db.sql"}, executionPhase = BEFORE_TEST_METHOD)
@Sql(value = {"/clean_db.sql"}, executionPhase = AFTER_TEST_METHOD)
public abstract class ControllerIntegrationTest {

    @LocalServerPort
    private int port;

    public static RequestSpecification requestSpecification(UriComponents uri) {
        return given().basePath(uri.getPath())
                      .queryParams(uri.getQueryParams());
    }

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }
}
