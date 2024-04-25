package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.ScheduledPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduledPaymentRepository extends JpaRepository<ScheduledPayment, String> {

    List<ScheduledPayment> findAllByDayOfWeek(Integer dayOfWeek);

    @Query("SELECT p FROM ScheduledPayment p WHERE p.dayOfMonth >= 28")
    List<ScheduledPayment> findForEndOfMonth();

    List<ScheduledPayment> findAllByDayOfMonth(int dayOfMonth);

    List<ScheduledPayment> findAllByDayOfYear(int min);
}
