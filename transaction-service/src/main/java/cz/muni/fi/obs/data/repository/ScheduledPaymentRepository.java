package cz.muni.fi.obs.data.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cz.muni.fi.obs.data.dbo.ScheduledPayment;

@Repository
public interface ScheduledPaymentRepository extends JpaRepository<ScheduledPayment, String> {

    @Query("SELECT p FROM ScheduledPayment p WHERE p.dayOfWeek = ?1 AND (p.validUntil is null OR p.validUntil > ?2)")
    List<ScheduledPayment> findAllByDayOfWeek(Integer dayOfWeek, Instant now);

    @Query("SELECT p FROM ScheduledPayment p WHERE p.dayOfMonth >= 28 AND (p.validUntil is null OR p.validUntil > ?1)")
    List<ScheduledPayment> findForEndOfMonth(Instant now);

    @Query("SELECT p FROM ScheduledPayment p WHERE p.dayOfMonth = ?1 AND (p.validUntil is null OR p.validUntil > ?2)")
    List<ScheduledPayment> findAllByDayOfMonth(int dayOfMonth, Instant now);

    @Query("SELECT p FROM ScheduledPayment p WHERE p.dayOfYear = ?1 AND (p.validUntil is null OR p.validUntil > ?2)")
    List<ScheduledPayment> findAllByDayOfYear(int dayOfYear, Instant now);
}
