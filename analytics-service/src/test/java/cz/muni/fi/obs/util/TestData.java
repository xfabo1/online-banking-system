package cz.muni.fi.obs.util;

import cz.muni.fi.obs.data.dbo.AccountDimension;
import cz.muni.fi.obs.data.dbo.DailyTransactionFact;
import cz.muni.fi.obs.data.dbo.DateDimension;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestData {
    public List<DailyTransactionFact> transactions = new ArrayList<>();
    private static final AccountDimension ACCOUNT_DIMENSION = new AccountDimension("1234567890");

    public TestData() {
        DailyTransactionFact transaction1 = new DailyTransactionFact(
                5,
                5,
                new BigDecimal(30000),
                new BigDecimal(20000),
                new BigDecimal(10000),
                new BigDecimal(4000),
                new BigDecimal(2000),
                ACCOUNT_DIMENSION,
                new DateDimension(2021, 1, 1),
                null
        );
        DailyTransactionFact transaction2 = new DailyTransactionFact(
                10,
                10,
                new BigDecimal(40000),
                new BigDecimal(20000),
                new BigDecimal(20000),
                new BigDecimal(2000),
                new BigDecimal(2000),
                ACCOUNT_DIMENSION,
                new DateDimension(2021, 1, 2),
                null
        );
        DailyTransactionFact transaction3 = new DailyTransactionFact(
                4,
                2,
                new BigDecimal(10000),
                new BigDecimal(8000),
                new BigDecimal(2000),
                new BigDecimal(2000),
                new BigDecimal(1000),
                ACCOUNT_DIMENSION,
                new DateDimension(2021, 1, 3),
                null
        );
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
    }
}
