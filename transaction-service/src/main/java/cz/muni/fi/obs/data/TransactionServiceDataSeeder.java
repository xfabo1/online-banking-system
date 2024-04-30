package cz.muni.fi.obs.data;

import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.data.repository.ScheduledPaymentRepository;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import cz.muni.fi.obs.service.TransactionService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnExpression("${data.initialize:false}")
@Slf4j
public class TransactionServiceDataSeeder {

    private final ScheduledPaymentRepository scheduledPaymentRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    @Autowired
    public TransactionServiceDataSeeder(AccountRepository accountRepository,
                                        TransactionRepository transactionRepository,
                                        ScheduledPaymentRepository scheduledPaymentRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.scheduledPaymentRepository = scheduledPaymentRepository;
        this.transactionService = transactionService;
    }

    @PostConstruct
    public void initializeData() {
        log.info("Initializing transaction service data...");

        List<AccountDbo> accounts = createAccounts();
        createScheduledPayments(accounts);

        transactionService

        createTransactions(accounts);
        log.info("Initialized transaction service data...");
    }

    // TODO: implement
    private void createTransactions(List<AccountDbo> accounts) {
        TransactionCreateDto transactionCreateDto = new TransactionCreateDto();

        transactionService.createTransaction(transactionCreateDto)
    }

    private List<AccountDbo> createAccounts() {
        AccountDbo accountDbo = new AccountDbo();

        accountRepository.save(accountDbo)
    }

    private void createScheduledPayments(List<AccountDbo> accounts) {
    }
}
