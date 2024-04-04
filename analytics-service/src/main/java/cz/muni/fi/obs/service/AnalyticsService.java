package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.DailySummary;
import cz.muni.fi.obs.api.DailySummaryResult;
import cz.muni.fi.obs.api.MonthlySummary;
import cz.muni.fi.obs.api.MonthlySummaryResult;
import cz.muni.fi.obs.data.AnalyticsRepository;
import cz.muni.fi.obs.data.dbo.DailyTransaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {
    private final AnalyticsRepository analyticsRepository;

    public AnalyticsService(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    public DailySummaryResult getDailySummary(String accountNumber, int year, int month) {
        List<DailyTransaction> transactions = analyticsRepository.getDailyTransactions(accountNumber, year, month);
        if (transactions.isEmpty()) {
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Month must be between 1 and 12");
            }
            return new DailySummaryResult(LocalDate.now(), new ArrayList<>());
        }
        return createDailySummaryResult(transactions);
    }
    public MonthlySummaryResult getMonthlySummary(String accountNumber, int year, int month) {
        List<DailyTransaction> transactions = analyticsRepository.getDailyTransactions(accountNumber, year, month);
        if (transactions.isEmpty()) {
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Month must be between 1 and 12");
            }
            return new MonthlySummaryResult(LocalDate.now(), new MonthlySummary(Month.of(month).name(), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0)));
        }
        return createMonthlySummaryResult(transactions);
    }

    private DailySummaryResult createDailySummaryResult(List<DailyTransaction> transactions) {
        List<DailySummary> dailySummaries = new ArrayList<>();
        transactions.forEach(transaction -> dailySummaries.add(createDailySummary(transaction)));
        return new DailySummaryResult(LocalDate.now(), dailySummaries);
    }
    private DailySummary createDailySummary(DailyTransaction transaction) {
        return new DailySummary(transaction.getDate().getDate(),
                transaction.getTotalWithdrawalTransactions(),
                transaction.getTotalDepositTransactions(),
                transaction.getTotalWithdrawalAmount(),
                transaction.getTotalDepositAmount(),
                transaction.getAverageWithdrawalAmount(),
                transaction.getAverageDepositAmount(),
                transaction.getTotalDepositAmount().subtract(transaction.getTotalWithdrawalAmount()));
    }

    private MonthlySummaryResult createMonthlySummaryResult(List<DailyTransaction> transactions) {
        return new MonthlySummaryResult(LocalDate.now(), createMonthlySummary(transactions));
    }
    private MonthlySummary createMonthlySummary(List<DailyTransaction> transactions) {
        String month = Month.of(transactions.getFirst().getDate().getMonth()).name();
        BigDecimal totalWithdrawalTransactions = new BigDecimal(0);
        BigDecimal totalDepositTransactions = new BigDecimal(0);
        BigDecimal totalWithdrawalAmount = new BigDecimal(0);
        BigDecimal totalDepositAmount = new BigDecimal(0);

        for (DailyTransaction transaction : transactions) {
            totalWithdrawalTransactions = totalWithdrawalTransactions.add(transaction.getTotalWithdrawalTransactions());
            totalDepositTransactions = totalDepositTransactions.add(transaction.getTotalDepositTransactions());
            totalWithdrawalAmount = totalWithdrawalAmount.add(transaction.getTotalWithdrawalAmount());
            totalDepositAmount = totalDepositAmount.add(transaction.getTotalDepositAmount());
        }
        BigDecimal averageWithdrawalAmount = totalWithdrawalAmount.divide(totalWithdrawalTransactions, 2, RoundingMode.HALF_UP);
        BigDecimal averageDepositAmount = totalDepositAmount.divide(totalDepositTransactions, 2, RoundingMode.HALF_UP);

        return new MonthlySummary(month,
                totalWithdrawalTransactions,
                totalDepositTransactions,
                totalWithdrawalAmount,
                totalDepositAmount,
                averageWithdrawalAmount,
                averageDepositAmount,
                totalDepositAmount.subtract(totalWithdrawalAmount));
    }
}
