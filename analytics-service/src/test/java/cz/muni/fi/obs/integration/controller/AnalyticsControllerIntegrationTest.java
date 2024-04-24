package cz.muni.fi.obs.integration.controller;

import cz.muni.fi.obs.Application;
import cz.muni.fi.obs.api.DailySummaryRequest;
import cz.muni.fi.obs.api.DailySummaryResult;
import cz.muni.fi.obs.data.AnalyticsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@Sql(value = {"/initialize_db.sql"}, executionPhase = BEFORE_TEST_CLASS)
@Sql(value = {"/drop_all.sql"}, executionPhase = AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
public class AnalyticsControllerIntegrationTest extends ControllerIntegrationTest {

    private static final String ANALYTICS_CONTROLLER_PATH = "/api/analytics_service/v1/{account_number}/";

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Test
    public void createDailySummary_ValidRequest_CreatesCorrectSummary() {

        UriComponents components = UriComponentsBuilder.
                fromPath(ANALYTICS_CONTROLLER_PATH + "daily-summary")
                .buildAndExpand("1234567890");

        DailySummaryRequest request = new DailySummaryRequest(2021, 1);

        DailySummaryResult dailySummaryResult = requestSpecification(components)
                .body(request)
                .post()
                .then()
                .statusCode(200)
                .extract()
                .as(DailySummaryResult.class);

        assertThat(dailySummaryResult.summaries()).hasSize(3);
    }
}