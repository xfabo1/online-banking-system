package cz.muni.fi.obs.util;

import cz.muni.fi.obs.data.dbo.Account;
import cz.muni.fi.obs.data.dbo.DailyTransaction;
import cz.muni.fi.obs.data.dbo.Date;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestData {
    public List<DailyTransaction> transactions = new ArrayList<>();
    private static final Account account = new Account("1234567890");

    public TestData() {
        DailyTransaction transaction1 = new DailyTransaction(
                5,
                5,
                new BigDecimal(30000),
                new BigDecimal(20000),
                new BigDecimal(10000),
                new BigDecimal(4000),
                new BigDecimal(2000),
                account,
                new Date(2021, 1, 1)
        );
        DailyTransaction transaction2 = new DailyTransaction(
                10,
                10,
                new BigDecimal(40000),
                new BigDecimal(20000),
                new BigDecimal(20000),
                new BigDecimal(2000),
                new BigDecimal(2000),
                account,
                new Date(2021, 1, 2)

        );
        DailyTransaction transaction3 = new DailyTransaction(
                4,
                2,
                new BigDecimal(10000),
                new BigDecimal(8000),
                new BigDecimal(2000),
                new BigDecimal(2000),
                new BigDecimal(1000),
                account,
                new Date(2021, 1, 3)
        );
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
    }
}
