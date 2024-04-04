package cz.muni.fi.obs.data;
import cz.muni.fi.obs.data.dbo.DailyTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class AnalyticsRepository {
    private final DataStore dataStore;

    @Autowired
    public AnalyticsRepository(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public List<DailyTransaction> getDailyTransactions(String accountNumber, int year, int month) {
        Stream<DailyTransaction> transactionStream = dataStore.transactions.stream()
                .filter(transaction -> transaction.getAccount().getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getDate().getYear() == year)
                .filter(transaction -> transaction.getDate().getMonth() == month);
        return transactionStream.toList();
    }
}
