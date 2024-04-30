package cz.muni.fi.obs.service.payment;

import cz.muni.fi.obs.data.dbo.ScheduledPayment;
import cz.muni.fi.obs.data.repository.ScheduledPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduledPaymentRetrievalService {

    private final ScheduledPaymentRepository repository;

    @Autowired
    public ScheduledPaymentRetrievalService(ScheduledPaymentRepository repository) {
        this.repository = repository;
    }

    public List<ScheduledPayment> findForCurrentDayOfWeek() {
        return repository.findAllByDayOfWeek(LocalDate.now().getDayOfWeek().ordinal(), Instant.now());
    }

    public List<ScheduledPayment> findForEndOfMonth() {
        return repository.findForEndOfMonth(Instant.now());
    }

    public List<ScheduledPayment> findForCurrentDayOfMonth() {
        return repository.findAllByDayOfMonth(LocalDate.now().getDayOfMonth(), Instant.now());
    }

    public List<ScheduledPayment> findForCurrentDayOfYear() {
        return repository.findAllByDayOfYear(Math.min(LocalDate.now().getDayOfYear(), 365), Instant.now());
    }
}

