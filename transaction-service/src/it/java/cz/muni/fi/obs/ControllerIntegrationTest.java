package cz.muni.fi.obs;

import static io.restassured.RestAssured.given;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponents;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = { "/initialize_db.sql" }, executionPhase = BEFORE_TEST_CLASS)
public abstract class ControllerIntegrationTest {

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
}
