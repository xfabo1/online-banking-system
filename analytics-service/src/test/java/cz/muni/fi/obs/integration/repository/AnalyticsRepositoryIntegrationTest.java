package cz.muni.fi.obs.integration.repository;

import cz.muni.fi.obs.Application;
import cz.muni.fi.obs.data.AnalyticsRepository;
import cz.muni.fi.obs.data.dbo.DailyTransactionFact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;


@Sql(value = {"/initialize_db.sql"}, executionPhase = BEFORE_TEST_CLASS)
@Sql(value = {"/drop_all.sql"}, executionPhase = AFTER_TEST_CLASS)
@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {Application.class})
public class AnalyticsRepositoryIntegrationTest {

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Test
    public void getDailyTransactions_TransactinsFound_ReturnsTransactions() {
        List<DailyTransactionFact> dailyTransactionFacts = analyticsRepository.getDailyTransactions("1234567890", 2021, 1);

        assertThat(dailyTransactionFacts.size()).isEqualTo(3);

        DailyTransactionFact firstDailyTransactionFact = dailyTransactionFacts.getFirst();

        assertThat(firstDailyTransactionFact.getTotalDepositTransactions()).isEqualTo(5);
        assertThat(firstDailyTransactionFact.getTotalWithdrawalTransactions()).isEqualTo(5);

        assertThat(firstDailyTransactionFact.getTotalTransactionAmount()).isEqualTo(new BigDecimal("30000.00"));
        assertThat(firstDailyTransactionFact.getTotalWithdrawalAmount()).isEqualTo(new BigDecimal("20000.00"));
        assertThat(firstDailyTransactionFact.getTotalDepositAmount()).isEqualTo(new BigDecimal("10000.00"));
        assertThat(firstDailyTransactionFact.getAverageWithdrawalAmount()).isEqualTo(new BigDecimal("4000.00"));
        assertThat(firstDailyTransactionFact.getAverageDepositAmount()).isEqualTo(new BigDecimal("2000.00"));

        assertThat(firstDailyTransactionFact.getDateDimension().getFullDate()).isEqualTo(LocalDate.of(2021, 1, 1));
        assertThat(firstDailyTransactionFact.getAccountDimension().getAccountNumber()).isEqualTo("1234567890");
    }

    @Test
    public void getDailyTransactions_NoTransactionsFound_ReturnsEmptyList() {
        List<DailyTransactionFact> dailyTransactionFacts = analyticsRepository.getDailyTransactions("1234567890", 2021, 2);

        assertThat(dailyTransactionFacts).isEmpty();
    }

    @Test
    public void getDailyTransactionsByAmountRange_TransactionsFound_ReturnsTransactions() {
        List<DailyTransactionFact> dailyTransactionFacts = analyticsRepository.getDailyTransactionsByAmountRange("1234567890", new BigDecimal("35000.00"), new BigDecimal("45000.00"));

        assertThat(dailyTransactionFacts).hasSize(1);

        DailyTransactionFact firstDailyTransactionFact = dailyTransactionFacts.getFirst();

        assertThat(firstDailyTransactionFact.getTotalDepositTransactions()).isEqualTo(10);
        assertThat(firstDailyTransactionFact.getTotalWithdrawalTransactions()).isEqualTo(10);

        assertThat(firstDailyTransactionFact.getTotalTransactionAmount()).isEqualTo(new BigDecimal("40000.00"));
        assertThat(firstDailyTransactionFact.getTotalWithdrawalAmount()).isEqualTo(new BigDecimal("20000.00"));
        assertThat(firstDailyTransactionFact.getTotalDepositAmount()).isEqualTo(new BigDecimal("20000.00"));
        assertThat(firstDailyTransactionFact.getAverageWithdrawalAmount()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(firstDailyTransactionFact.getAverageDepositAmount()).isEqualTo(new BigDecimal("2000.00"));

        assertThat(firstDailyTransactionFact.getDateDimension().getFullDate()).isEqualTo(LocalDate.of(2021, 1, 2));
        assertThat(firstDailyTransactionFact.getAccountDimension().getAccountNumber()).isEqualTo("1234567890");
    }

    @Test
    public void getDailyTransactionsByAmountRange_NoTransactionsFound_ReturnsEmptyList() {
        List<DailyTransactionFact> dailyTransactionFacts = analyticsRepository.getDailyTransactionsByAmountRange("1234567890", new BigDecimal("45000.00"), new BigDecimal("50000.00"));

        assertThat(dailyTransactionFacts).isEmpty();
    }
}