package cz.muni.fi.obs.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailySummary(LocalDate date,
                           Integer totalWithdrawalTransactions,
                           Integer totalDepositTransactions,
                           BigDecimal totalWithdrawn,
                           BigDecimal totalDeposited,
                           BigDecimal averageWithdrawn,
                           BigDecimal averageDeposited,
                           BigDecimal difference) {
}
