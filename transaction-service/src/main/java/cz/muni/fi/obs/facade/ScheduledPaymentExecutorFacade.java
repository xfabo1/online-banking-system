package cz.muni.fi.obs.facade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.ScheduledPayment;
import cz.muni.fi.obs.service.TransactionService;
import cz.muni.fi.obs.service.payment.ScheduledPaymentRetrievalService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class ScheduledPaymentExecutorFacade {

    private static final Integer END_OF_MONTH_PAYMENT_DAY = 28;

    private static final String SCHEDULER_NOTE = "Automatic payment.";

    private final ScheduledPaymentRetrievalService retrievalService;

    private final TransactionService transactionService;


    @Autowired
    public ScheduledPaymentExecutorFacade(ScheduledPaymentRetrievalService retrievalService,
                                          TransactionService transactionService) {
        this.retrievalService = retrievalService;
        this.transactionService = transactionService;
    }

    @Scheduled(cron = "0 10 1 * * *")
    public void executeWeekly() {
        log.info("Executing weekly payments...");
        executeReadyPayments(retrievalService.findForCurrentDayOfWeek());
        log.info("Executed weekly payments...");
    }

    @Scheduled(cron = "0 20 1 * * *")
    public void executeMonthly() {
        log.info("Executing monthly payments...");
        LocalDate date = LocalDate.now();
        int today = date.getDayOfMonth();
        int tommorrow = date.plusDays(1).getDayOfMonth();

        List<ScheduledPayment> ready = new ArrayList<>();

        // if today is >= 28 only execute payments if its last day of month
        if (today >= END_OF_MONTH_PAYMENT_DAY) {
            if (tommorrow < today) {
                ready = retrievalService.findForEndOfMonth();
            }
        } else {
            ready = retrievalService.findForCurrentDayOfMonth();
        }

        executeReadyPayments(ready);
        log.info("Executed monthly payments...");
    }

    @Scheduled(cron = "0 30 1 * * *")
    public void executeYearly() {
        log.info("Executing yearly payments...");
        executeReadyPayments(retrievalService.findForCurrentDayOfYear());
        log.info("Executed yearly payments...");
    }

    private void executeReadyPayments(List<ScheduledPayment> ready) {
        ready.forEach(payment -> {
            TransactionCreateDto transactionCreateDto = new TransactionCreateDto(payment.getWithdrawsFrom().getId(), payment.getDepositsTo().getId(), payment.getAmount(),
                    SCHEDULER_NOTE, "");
            transactionService.createTransaction(transactionCreateDto);
        });
    }
}
