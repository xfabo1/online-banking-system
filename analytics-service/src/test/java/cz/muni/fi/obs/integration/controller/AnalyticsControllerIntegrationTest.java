package cz.muni.fi.obs.integration.controller;

import cz.muni.fi.obs.Application;
import cz.muni.fi.obs.api.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@Sql(value = {"/initialize_db.sql"}, executionPhase = BEFORE_TEST_CLASS)
@Sql(value = {"/drop_all.sql"}, executionPhase = AFTER_TEST_CLASS)
@ContextConfiguration(classes = {Application.class})
public class AnalyticsControllerIntegrationTest extends ControllerIntegrationTest {

    private static final String ANALYTICS_CONTROLLER_PATH = "/api/analytics-service/v1/{account_number}/";

    @Test
    void createDailySummary_ValidRequest_CreatesCorrectSummary() {
        UriComponents components = UriComponentsBuilder.
                fromPath(ANALYTICS_CONTROLLER_PATH + "daily-summary")
                .buildAndExpand("1234567890");

        DailySummaryRequest request = new DailySummaryRequest(2021, 1);

        DailySummaryResult dailySummaryResult = requestSpecification(components)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .post()
                .then()
                .statusCode(200)
                .extract()
                .as(DailySummaryResult.class);

        assertThat(dailySummaryResult.summaries()).hasSize(3);

        DailySummary firstSummary = dailySummaryResult.summaries().getFirst();

        assertThat(firstSummary.date()).isEqualTo("2021-01-01");
        assertThat(firstSummary.totalWithdrawalTransactions()).isEqualTo(5);
        assertThat(firstSummary.totalDepositTransactions()).isEqualTo(5);
        assertThat(firstSummary.totalWithdrawn()).isEqualTo("20000.00");
        assertThat(firstSummary.totalDeposited()).isEqualTo("10000.00");
        assertThat(firstSummary.averageWithdrawn()).isEqualTo("4000.00");
        assertThat(firstSummary.averageDeposited()).isEqualTo("2000.00");
        assertThat(firstSummary.difference()).isEqualTo("-10000.00");
    }

    @Test
    void createDailySummary_InvalidRequest_ReturnsBadRequest() {
        UriComponents components = UriComponentsBuilder.
                fromPath(ANALYTICS_CONTROLLER_PATH + "daily-summary")
                .buildAndExpand("1234567890");

        DailySummaryRequest request = new DailySummaryRequest(2021, 13);

        requestSpecification(components)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    void createMonthlySummary_ValidRequest_CreatesCorrectSummary() {
        UriComponents components = UriComponentsBuilder.
                fromPath(ANALYTICS_CONTROLLER_PATH + "monthly-summary")
                .buildAndExpand("1234567890");

        MonthlySummaryRequest request = new MonthlySummaryRequest(2021, 1);

        MonthlySummaryResult monthlySummaryResult = requestSpecification(components)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .post()
                .then()
                .statusCode(200)
                .extract()
                .as(MonthlySummaryResult.class);

        MonthlySummary summary = monthlySummaryResult.summary();

        assertThat(summary.month()).isEqualTo("JANUARY");
        assertThat(summary.totalWithdrawalTransactions()).isEqualTo(19);
        assertThat(summary.totalDepositTransactions()).isEqualTo(17);
        assertThat(summary.totalWithdrawn()).isEqualTo("48000.00");
        assertThat(summary.totalDeposited()).isEqualTo("32000.00");
        assertThat(summary.averageWithdrawn()).isEqualTo("2526.32");
        assertThat(summary.averageDeposited()).isEqualTo("1882.35");
        assertThat(summary.difference()).isEqualTo("-16000.00");
    }

    @Test
    void createMonthlySummary_InvalidRequest_ReturnsBadRequest() {
        UriComponents components = UriComponentsBuilder.
                fromPath(ANALYTICS_CONTROLLER_PATH + "monthly-summary")
                .buildAndExpand("1234567890");

        MonthlySummaryRequest request = new MonthlySummaryRequest(2021, 13);

        requestSpecification(components)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .post()
                .then()
                .statusCode(400);
    }
}