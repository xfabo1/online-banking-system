package cz.muni.fi.obs.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

import cz.muni.fi.obs.api.ScheduledPaymentCreateDto;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.PaymentFrequency;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.data.repository.ScheduledPaymentRepository;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import cz.muni.fi.obs.facade.TransactionManagementFacade;
import cz.muni.fi.obs.service.TransactionService;
import cz.muni.fi.obs.service.payment.ScheduledPaymentService;
import cz.muni.fi.obs.util.Resources;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnExpression("${data.initialize:false}")
@Slf4j
public class TransactionServiceDataSeeder {

    private final AccountRepository accountRepository;
    private final ScheduledPaymentService scheduledPaymentService;
    private final TransactionManagementFacade transactionManagementFacade;

    @Autowired
    public TransactionServiceDataSeeder(AccountRepository accountRepository,
                                        TransactionRepository transactionRepository,
                                        ScheduledPaymentRepository scheduledPaymentRepository,
                                        TransactionService transactionService,
                                        ScheduledPaymentService scheduledPaymentService,
                                        TransactionManagementFacade transactionManagementFacade) {
        this.accountRepository = accountRepository;
        this.scheduledPaymentService = scheduledPaymentService;
        this.transactionManagementFacade = transactionManagementFacade;
    }

    @PostConstruct
    public void initializeData() {
        log.info("Initializing transaction service data...");

        List<AccountDbo> accounts = createAccounts();
        createTransactions(accounts);
        createScheduledPayments(accounts);

        log.info("Initialized transaction service data...");
    }

    private void createTransactions(List<AccountDbo> accounts) {
        transactionManagementFacade.createTransaction(new TransactionCreateDto(
                accounts.get(0).getId(),
                accounts.getLast().getId(),
                new BigDecimal(10000),
                "Test transaction 1",
                "123456"
        ));
        transactionManagementFacade.createTransaction(new TransactionCreateDto(
                accounts.get(2).getId(),
                accounts.get(3).getId(),
                new BigDecimal(20000),
                "Test transaction 2",
                "654321"
        ));
        transactionManagementFacade.createTransaction(new TransactionCreateDto(
                accounts.get(3).getId(),
                accounts.get(4).getId(),
                new BigDecimal(30000),
                "Test transaction 3",
                "987654"
        ));
    }

    private List<AccountDbo> createAccounts() {
        String path = "/initialization_data/accounts.json";
        List<AccountDbo> accountDbos = Resources.readResource(path, new TypeReference<>(){});

		accountRepository.saveAll(accountDbos);

        return accountDbos;
    }

    private void createScheduledPayments(List<AccountDbo> accounts) {
        scheduledPaymentService.createPayment(new ScheduledPaymentCreateDto(
                LocalDate.of(2024, 6, 6),
                null,
                PaymentFrequency.YEARLY,
                accounts.getFirst().getId(),
                accounts.getLast().getId()
        ));
        scheduledPaymentService.createPayment(new ScheduledPaymentCreateDto(
                LocalDate.of(2024, 7, 7),
                null,
                PaymentFrequency.MONTHLY,
                accounts.get(1).getId(),
                accounts.get(2).getId()
        ));
        scheduledPaymentService.createPayment(new ScheduledPaymentCreateDto(
                LocalDate.of(2024, 8, 8),
                null,
                PaymentFrequency.WEEKLY,
                accounts.get(3).getId(),
                accounts.get(4).getId()
        ));
    }
}
