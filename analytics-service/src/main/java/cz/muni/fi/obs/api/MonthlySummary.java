package cz.muni.fi.obs.api;

import java.math.BigDecimal;

public record MonthlySummary(String month,
                             Integer totalWithdrawalTransactions,
                             Integer totalDepositTransactions,
                             BigDecimal totalWithdrawn,
                             BigDecimal totalDeposited,
                             BigDecimal averageWithdrawn,
                             BigDecimal averageDeposited,
                             BigDecimal difference) { }
