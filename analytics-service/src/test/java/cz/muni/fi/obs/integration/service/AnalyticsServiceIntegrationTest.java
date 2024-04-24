package cz.muni.fi.obs.integration.service;

import cz.muni.fi.obs.Application;
import cz.muni.fi.obs.api.DailySummaryResult;
import cz.muni.fi.obs.api.MonthlySummaryResult;
import cz.muni.fi.obs.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@ContextConfiguration(classes = {Application.class})
@Sql(value = {"/initialize_db.sql"}, executionPhase = BEFORE_TEST_CLASS)
@Sql(value = {"/drop_all.sql"}, executionPhase = AFTER_TEST_CLASS)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
class AnalyticsServiceIntegrationTest {

    @Autowired
    private AnalyticsService analyticsService;

    @Test
    void getDailySummary_noFactsPresentForAccount_createsEmptySummary() {
        DailySummaryResult dailySummary = analyticsService.getDailySummary("12345", 2023, 10);

        assertThat(dailySummary.summaries()).isEmpty();
    }

    @Test
    void getDailySummary_withFactsPresent_createsCorrectSummary() {
        DailySummaryResult dailySummaryResult = analyticsService.getDailySummary("1234567890", 2021, 1);

        assertThat(dailySummaryResult.summaries()).hasSize(3);
    }

    @Test
    void getMonthlySummary_noFactsPresentForAccount_createsEmptySummary() {
        MonthlySummaryResult monthlySummary = analyticsService.getMonthlySummary("12345678900", 2021, 1);

        assertThat(monthlySummary.summary()).isNotNull();
        assertThat(monthlySummary.summary().month()).isEqualTo(Month.of(1).toString());
        assertThat(monthlySummary.summary().totalWithdrawalTransactions()).isZero();
        assertThat(monthlySummary.summary().totalDepositTransactions()).isZero();
        assertThat(monthlySummary.summary().totalWithdrawn()).isZero();
        assertThat(monthlySummary.summary().totalDeposited()).isZero();
        assertThat(monthlySummary.summary().averageWithdrawn()).isZero();
        assertThat(monthlySummary.summary().averageDeposited()).isZero();
        assertThat(monthlySummary.summary().difference()).isZero();
    }

    @Test
    void getMonthlySummary_withFactsPresent_createsCorrectSummary() {
        MonthlySummaryResult monthlySummary = analyticsService.getMonthlySummary("1234567890", 2021, 1);

        assertThat(monthlySummary.summary()).isNotNull();
        assertThat(monthlySummary.summary().month()).isEqualTo(Month.of(1).toString());
        assertThat(monthlySummary.summary().totalWithdrawalTransactions()).isEqualTo(19);
        assertThat(monthlySummary.summary().totalDepositTransactions()).isEqualTo(17);
        assertThat(monthlySummary.summary().totalWithdrawn()).isEqualByComparingTo(BigDecimal.valueOf(48000));
        assertThat(monthlySummary.summary().totalDeposited()).isEqualByComparingTo(BigDecimal.valueOf(32000));
        assertThat(monthlySummary.summary().averageWithdrawn()).isEqualByComparingTo(BigDecimal.valueOf(2526.32));
        assertThat(monthlySummary.summary().averageDeposited()).isEqualByComparingTo(BigDecimal.valueOf(1882.35));
        assertThat(monthlySummary.summary().difference()).isEqualByComparingTo(BigDecimal.valueOf(-16000));
    }
}