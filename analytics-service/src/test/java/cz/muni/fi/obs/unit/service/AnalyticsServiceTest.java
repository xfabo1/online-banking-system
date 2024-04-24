package cz.muni.fi.obs.unit.service;

import cz.muni.fi.obs.api.DailySummaryResult;
import cz.muni.fi.obs.api.MonthlySummaryResult;
import cz.muni.fi.obs.data.AnalyticsRepository;
import cz.muni.fi.obs.service.AnalyticsService;
import cz.muni.fi.obs.util.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    private final TestData testData = new TestData();

    @Mock
    private AnalyticsRepository analyticsRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void getDailySummary_noFactsPresentForAccount_createsEmptySummary() {
        when(analyticsRepository.getDailyTransactions(any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new ArrayList<>());

        DailySummaryResult dailySummary = analyticsService.getDailySummary("12345", 2023, 10);

        verify(analyticsRepository).getDailyTransactions("12345", 2023, 10);
        assertThat(dailySummary.summaries().size()).isEqualTo(0);
    }

    @Test
    void getDailySummary_withFactsPresent_createsCorrectSummary() {
        when(analyticsRepository.getDailyTransactions(any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(testData.transactions);

        DailySummaryResult dailySummaryResult = analyticsService.getDailySummary("1234567890", 2023, 10);

        verify(analyticsRepository).getDailyTransactions("1234567890", 2023, 10);
        assertThat(dailySummaryResult.summaries().size()).isEqualTo(3);
    }

    @Test
    void getMonthlySummary_noFactsPresentForAccount_createsEmptySummary() {
        when(analyticsRepository.getDailyTransactions(any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new ArrayList<>());

        MonthlySummaryResult monthlySummary = analyticsService.getMonthlySummary("1234567890", 2021, 1);

        verify(analyticsRepository).getDailyTransactions("1234567890", 2021, 1);
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
        when(analyticsRepository.getDailyTransactions(any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(testData.transactions);

        MonthlySummaryResult monthlySummary = analyticsService.getMonthlySummary("1234567890", 2021, 1);

        verify(analyticsRepository).getDailyTransactions("1234567890", 2021, 1);
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
