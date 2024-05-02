package cz.muni.fi.obs.data;

import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.data.repository.ScheduledPaymentRepository;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import cz.muni.fi.obs.service.TransactionService;
import cz.muni.fi.obs.util.JsonConvertor;
import cz.muni.fi.obs.util.Resources;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.flywaydb.core.internal.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

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

        log.info("Initialized transaction service data...");
    }

    private void createTransactions(List<AccountDbo> accounts) {
    }

    private List<AccountDbo> createAccounts() {
        String path = "/initialization_data/accounts.json";
        List<AccountDbo> accountDbos = Resources.readResource(path, new TypeReference<>(){});

		accountRepository.saveAll(accountDbos);

        return accountDbos;
    }

    private void createScheduledPayments(List<AccountDbo> accounts) {
    }
}
