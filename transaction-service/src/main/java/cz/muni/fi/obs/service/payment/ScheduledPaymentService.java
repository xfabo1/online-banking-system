package cz.muni.fi.obs.service.payment;


import cz.muni.fi.obs.api.ScheduledPaymentCreateDto;
import cz.muni.fi.obs.api.ScheduledPaymentDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.PaymentFrequency;
import cz.muni.fi.obs.data.dbo.ScheduledPayment;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.data.repository.ScheduledPaymentRepository;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.facade.ScheduledPaymentFacade;
import cz.muni.fi.obs.mapper.ScheduledPaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
@Transactional
public class ScheduledPaymentService implements ScheduledPaymentFacade {

    private final ScheduledPaymentRepository scheduledPaymentRepository;

    private final AccountRepository accountRepository;

    private final ScheduledPaymentMapper mapper;

    @Autowired
    public ScheduledPaymentService(ScheduledPaymentRepository scheduledPaymentRepository,
                                   AccountRepository accountRepository,
                                   ScheduledPaymentMapper mapper) {
        this.scheduledPaymentRepository = scheduledPaymentRepository;
        this.accountRepository = accountRepository;
        this.mapper = mapper;
    }

    @Override
    public ScheduledPaymentDto createPayment(ScheduledPaymentCreateDto createDto) {
        AccountDbo from = accountRepository.findById(createDto.withdrawsFromId())
                .orElseThrow(() -> new ResourceNotFoundException(AccountDbo.class, createDto.withdrawsFromId()));

        AccountDbo to = accountRepository.findById(createDto.depositsToId())
                .orElseThrow(() -> new ResourceNotFoundException(AccountDbo.class, createDto.depositsToId()));

        ScheduledPayment scheduledPayment = new ScheduledPayment();
        scheduledPayment.setValidUntil(createDto.validUntil());
        scheduledPayment.setWithdrawsFrom(from);
        scheduledPayment.setDepositsTo(to);
        setTiming(scheduledPayment, createDto.frequency(), createDto.executeDate());

        return mapper.toDto(scheduledPaymentRepository.save(scheduledPayment));
    }

    @Override
    public void disablePayment(String id) {
        ScheduledPayment scheduledPayment = scheduledPaymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                ScheduledPayment.class, id
        ));

        scheduledPayment.setValidUntil(Instant.now());

        scheduledPaymentRepository.save(scheduledPayment);
    }

    @Override
    public void enablePayment(String id) {
        ScheduledPayment scheduledPayment = scheduledPaymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                ScheduledPayment.class, id
        ));

        scheduledPayment.setValidUntil(null);

        scheduledPaymentRepository.save(scheduledPayment);
    }

    private void setTiming(ScheduledPayment scheduledPayment, PaymentFrequency frequency, LocalDate executeDate) {
        switch (frequency) {
            case WEEKLY -> {
                scheduledPayment.setDayOfWeek(executeDate.getDayOfWeek().ordinal());
                return;
            }
            case MONTHLY -> {
                scheduledPayment.setDayOfMonth(executeDate.getDayOfMonth());
                return;
            }
            case YEARLY -> {
                scheduledPayment.setDayOfYear(Math.min(executeDate.getDayOfYear(), 365));
                return;
            }
        }
    }
}
