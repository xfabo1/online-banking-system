package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.DailyTransactionFact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AnalyticsRepository extends JpaRepository<DailyTransactionFact, String> {
    @Query("SELECT dt FROM DailyTransactionFact dt WHERE dt.accountDimension.accountNumber = ?1 AND dt.dateDimension.yearNumber = ?2 AND dt.dateDimension.monthNumber = ?3")
    List<DailyTransactionFact> getDailyTransactions(String accountNumber, int year, int month);

    @Query("SELECT dt FROM DailyTransactionFact dt WHERE dt.accountDimension.accountNumber = ?1 AND dt.totalTransactionAmount BETWEEN ?2 AND ?3")
    List<DailyTransactionFact> getDailyTransactionsByAmountRange(String accountNumber, BigDecimal minAmount, BigDecimal maxAmount);

}
