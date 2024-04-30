package cz.muni.fi.obs;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureObservability
public class TransactionServiceTest {

	@LocalManagementPort
	private int managementPort;

	@Test
	void healthCheckWorks() {
		String healthUrl = "http://localhost:" + managementPort + "/api/transaction-service/actuator/health";

		String status = get(healthUrl)
				.then()
				.extract()
				.path("status").toString();

		assertThat(status).isEqualTo("UP");
	}
}
