package cz.muni.fi.obs.data;
import cz.muni.fi.obs.data.dbo.DailyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnalyticsRepository extends JpaRepository<DailyTransaction, String>{
    @Query("SELECT dt FROM DailyTransaction dt WHERE dt.account.accountNumber = ?1 AND dt.date.year = ?2 AND dt.date.month = ?3")
    default List<DailyTransaction> getDailyTransactions(String accountNumber, int year, int month){
        return findAll();
    }
}
