package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.api.CurrencyExchangeRequest;
import cz.muni.fi.obs.api.CurrencyExchangeResult;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.ScheduledPayment;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.dbo.TransactionState;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.data.repository.ScheduledPaymentRepository;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import cz.muni.fi.obs.http.CurrencyServiceClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ScheduledPaymentExecutorFacadeTest {

    @Autowired
    private ScheduledPaymentExecutorFacade executorFacade;

    @Autowired
    private ScheduledPaymentRepository scheduledPaymentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @MockBean
    private CurrencyServiceClient client;

    private static final String SCHEDULER_NOTE = "Automatic payment.";

    @BeforeEach
    public void setUp() {
        when(client.getCurrencyExchange(any(CurrencyExchangeRequest.class)))
                .thenReturn(Optional.of(new CurrencyExchangeResult("CZK",
                        "CZK",
                        1d,
                        BigDecimal.valueOf(1000),
                        BigDecimal.valueOf(1000))));

        if (scheduledPaymentRepository.count() > 0) {
            return;
        }

        AccountDbo accountDbo = new AccountDbo();
        accountDbo.setId("123");
        accountDbo.setAccountNumber("1233");
        accountDbo.setCurrencyCode("CZK");
        accountDbo.setCustomerId("mikoflosso");

        accountDbo = accountRepository.save(accountDbo);

        AccountDbo accountDbo1 = new AccountDbo();
        accountDbo1.setId("1234");
        accountDbo1.setAccountNumber("12356");
        accountDbo1.setCurrencyCode("CZK");
        accountDbo1.setCustomerId("ego");

        accountDbo1 = accountRepository.save(accountDbo1);

        BigDecimal amount = BigDecimal.valueOf(1000);
        Instant past = Instant.now().minusSeconds(1000);

        // ONCE A MONTH
        ScheduledPayment scheduledPayment = new ScheduledPayment();
        scheduledPayment.setDayOfMonth(LocalDate.now().getDayOfMonth());
        scheduledPayment.setAmount(amount);
        scheduledPayment.setWithdrawsFrom(accountDbo);
        scheduledPayment.setDepositsTo(accountDbo1);
        scheduledPaymentRepository.save(scheduledPayment);

        ScheduledPayment scheduledPayment1 = new ScheduledPayment();
        scheduledPayment1.setDayOfMonth(LocalDate.now().getDayOfMonth());
        scheduledPayment1.setAmount(amount);
        scheduledPayment1.setWithdrawsFrom(accountDbo);
        scheduledPayment1.setDepositsTo(accountDbo1);
        scheduledPayment1.setValidUntil(past);
        scheduledPaymentRepository.save(scheduledPayment1);

        // ONCE A WEEK
        ScheduledPayment scheduledPayment2 = new ScheduledPayment();
        scheduledPayment2.setDayOfWeek(LocalDate.now().getDayOfWeek().ordinal());
        scheduledPayment2.setAmount(amount);
        scheduledPayment2.setWithdrawsFrom(accountDbo);
        scheduledPayment2.setDepositsTo(accountDbo1);
        scheduledPaymentRepository.save(scheduledPayment2);

        ScheduledPayment scheduledPayment3 = new ScheduledPayment();
        scheduledPayment3.setDayOfWeek(LocalDate.now().getDayOfWeek().ordinal());
        scheduledPayment3.setAmount(amount);
        scheduledPayment3.setWithdrawsFrom(accountDbo);
        scheduledPayment3.setDepositsTo(accountDbo1);
        scheduledPayment3.setValidUntil(past);
        scheduledPaymentRepository.save(scheduledPayment3);

        // ONCE A YEAR
        ScheduledPayment scheduledPayment4 = new ScheduledPayment();
        scheduledPayment4.setDayOfYear(LocalDate.now().getDayOfYear());
        scheduledPayment4.setAmount(amount);
        scheduledPayment4.setWithdrawsFrom(accountDbo);
        scheduledPayment4.setDepositsTo(accountDbo1);
        scheduledPaymentRepository.save(scheduledPayment4);

        ScheduledPayment scheduledPayment5 = new ScheduledPayment();
        scheduledPayment5.setDayOfYear(LocalDate.now().getDayOfYear());
        scheduledPayment5.setAmount(amount);
        scheduledPayment5.setWithdrawsFrom(accountDbo);
        scheduledPayment5.setDepositsTo(accountDbo1);
        scheduledPayment5.setValidUntil(past);
        scheduledPaymentRepository.save(scheduledPayment5);
    }

    @AfterEach
    public void cleanTransactions() {
        transactionRepository.deleteAll();
    }

    @Test
    public void executeWeekly_weeklyPaymentsExistForToday_paymentsAreExecuted() {
        executorFacade.executeWeekly();

        List<TransactionDbo> transactions = transactionRepository.findAll();

        assertThat(transactions.size()).isEqualTo(1);

        TransactionDbo first = transactions.getFirst();

        assertThat(first.getWithdrawsFrom().getId()).isEqualTo("123");
        assertThat(first.getDepositsTo().getId()).isEqualTo("1234");
        assertThat(first.getNote()).isEqualTo(SCHEDULER_NOTE);
        assertThat(first.getTransactionState()).isEqualTo(TransactionState.FAILED);
    }

    @Test
    public void executeMonthly_monthlyPaymentsExistForToday_paymentsAreExecuted() {
        executorFacade.executeMonthly();

        List<TransactionDbo> transactions = transactionRepository.findAll();

        assertThat(transactions.size()).isEqualTo(1);

        TransactionDbo first = transactions.getFirst();

        assertThat(first.getWithdrawsFrom().getId()).isEqualTo("123");
        assertThat(first.getDepositsTo().getId()).isEqualTo("1234");
        assertThat(first.getNote()).isEqualTo(SCHEDULER_NOTE);
        assertThat(first.getTransactionState()).isEqualTo(TransactionState.FAILED);
    }

    @Test
    public void executeYearly_yearlyPaymentsExistForToday_paymentsAreExecuted() {
        executorFacade.executeYearly();

        List<TransactionDbo> transactions = transactionRepository.findAll();

        assertThat(transactions.size()).isEqualTo(1);

        TransactionDbo first = transactions.getFirst();

        assertThat(first.getWithdrawsFrom().getId()).isEqualTo("123");
        assertThat(first.getDepositsTo().getId()).isEqualTo("1234");
        assertThat(first.getNote()).isEqualTo(SCHEDULER_NOTE);
        assertThat(first.getTransactionState()).isEqualTo(TransactionState.FAILED);
    }
}