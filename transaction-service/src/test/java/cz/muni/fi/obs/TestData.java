package cz.muni.fi.obs;

import cz.muni.fi.obs.data.dbo.TransactionDbo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TestData {
    public static final String accountId = "123456-7890123456/0100";
    public static final List<TransactionDbo> withdrawTransactions = Arrays.asList(
            TransactionDbo.builder()
                    .id("1")
                    .depositsTo("987654-3210987654/0300")
                    .conversionRate(0.25)
                    .note("note")
                    .withdrawAmount(BigDecimal.valueOf(1000))
                    .withdrawsFrom(accountId)
                    .depositAmount(BigDecimal.valueOf(250))
                    .variableSymbol("123")
                    .build(),
            TransactionDbo.builder()
                    .id("2")
                    .depositsTo("111111-1111111111/0710")
                    .conversionRate(2.0)
                    .note("note")
                    .withdrawAmount(BigDecimal.valueOf(2))
                    .withdrawsFrom(accountId)
                    .depositAmount(BigDecimal.valueOf(2))
                    .variableSymbol("123")
                    .build()
    );

    public static final List<TransactionDbo> depositTransactions = Arrays.asList(
            TransactionDbo.builder()
                    .id("3")
                    .depositsTo(accountId)
                    .conversionRate(3.0)
                    .note("note")
                    .withdrawAmount(BigDecimal.valueOf(1000.5))
                    .withdrawsFrom("111111-1111121111/0710")
                    .depositAmount(BigDecimal.valueOf(3001.5))
                    .variableSymbol("123")
                    .build(),

            TransactionDbo.builder()
                    .id("4")
                    .depositsTo(accountId)
                    .conversionRate(11.0)
                    .note("note")
                    .withdrawAmount(BigDecimal.valueOf(4))
                    .withdrawsFrom("111111-1111111111/0710")
                    .depositAmount(BigDecimal.valueOf(44))
                    .variableSymbol("123")
                    .build()
    );
}
