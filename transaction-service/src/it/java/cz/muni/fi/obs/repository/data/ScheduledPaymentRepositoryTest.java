package cz.muni.fi.obs.repository.data;

import cz.muni.fi.obs.common.PostgresqlTest;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.ScheduledPayment;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.data.repository.ScheduledPaymentRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ScheduledPaymentRepositoryTest extends PostgresqlTest {

    @Autowired
    private ScheduledPaymentRepository scheduledPaymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        AccountDbo accountDbo = new AccountDbo();
        accountDbo.setAccountNumber("1233");
        accountDbo.setCurrencyCode("CZK");
        accountDbo.setCustomerId("mikoflosso");

        accountDbo = accountRepository.save(accountDbo);

        AccountDbo accountDbo1 = new AccountDbo();
        accountDbo1.setAccountNumber("12356");
        accountDbo1.setCurrencyCode("CZK");
        accountDbo1.setCustomerId("ego");

        accountDbo1 = accountRepository.save(accountDbo1);

        BigDecimal amount = BigDecimal.valueOf(1000);
        Instant past = Instant.now().minusSeconds(300);

        // ONCE A MONTH
        ScheduledPayment scheduledPayment = new ScheduledPayment();
        scheduledPayment.setDayOfMonth(1);
        scheduledPayment.setAmount(amount);
        scheduledPayment.setWithdrawsFrom(accountDbo);
        scheduledPayment.setDepositsTo(accountDbo1);
        scheduledPaymentRepository.save(scheduledPayment);

        ScheduledPayment scheduledPayment1 = new ScheduledPayment();
        scheduledPayment1.setDayOfMonth(1);
        scheduledPayment1.setAmount(amount);
        scheduledPayment1.setWithdrawsFrom(accountDbo);
        scheduledPayment1.setDepositsTo(accountDbo1);
        scheduledPayment1.setValidUntil(past);
        scheduledPaymentRepository.save(scheduledPayment1);

        // ONCE A WEEK
        ScheduledPayment scheduledPayment2 = new ScheduledPayment();
        scheduledPayment2.setDayOfWeek(1);
        scheduledPayment2.setAmount(amount);
        scheduledPayment2.setWithdrawsFrom(accountDbo);
        scheduledPayment2.setDepositsTo(accountDbo1);
        scheduledPaymentRepository.save(scheduledPayment2);

        ScheduledPayment scheduledPayment3 = new ScheduledPayment();
        scheduledPayment3.setDayOfWeek(1);
        scheduledPayment3.setAmount(amount);
        scheduledPayment3.setWithdrawsFrom(accountDbo);
        scheduledPayment3.setDepositsTo(accountDbo1);
        scheduledPayment3.setValidUntil(past);
        scheduledPaymentRepository.save(scheduledPayment3);

        // ONCE A YEAR
        ScheduledPayment scheduledPayment4 = new ScheduledPayment();
        scheduledPayment4.setDayOfYear(1);
        scheduledPayment4.setAmount(amount);
        scheduledPayment4.setWithdrawsFrom(accountDbo);
        scheduledPayment4.setDepositsTo(accountDbo1);
        scheduledPaymentRepository.save(scheduledPayment4);

        ScheduledPayment scheduledPayment5 = new ScheduledPayment();
        scheduledPayment5.setDayOfYear(1);
        scheduledPayment5.setAmount(amount);
        scheduledPayment5.setWithdrawsFrom(accountDbo);
        scheduledPayment5.setDepositsTo(accountDbo1);
        scheduledPayment5.setValidUntil(past);
        scheduledPaymentRepository.save(scheduledPayment5);

        // END OF MONTH
        ScheduledPayment scheduledPayment6 = new ScheduledPayment();
        scheduledPayment6.setDayOfMonth(30);
        scheduledPayment6.setAmount(amount);
        scheduledPayment6.setWithdrawsFrom(accountDbo);
        scheduledPayment6.setDepositsTo(accountDbo1);
        scheduledPaymentRepository.save(scheduledPayment6);

        ScheduledPayment scheduledPayment7 = new ScheduledPayment();
        scheduledPayment7.setDayOfWeek(30);
        scheduledPayment7.setAmount(amount);
        scheduledPayment7.setWithdrawsFrom(accountDbo);
        scheduledPayment7.setDepositsTo(accountDbo1);
        scheduledPayment7.setValidUntil(past);
        scheduledPaymentRepository.save(scheduledPayment7);
    }

    @Test
    void findAllByDayOfWeek_oneTransactionExists_findsCorrectTransaction() {
        List<ScheduledPayment> allByDayOfWeek =
                scheduledPaymentRepository.findAllByDayOfWeek(1, Instant.now());

        assertThat(allByDayOfWeek.size()).isEqualTo(1);
        assertThat(allByDayOfWeek.getFirst().getValidUntil()).isNull();
    }

    @Test
    void findForEndOfMonth_oneTransactionExists_findsCorrectTransaction() {
        List<ScheduledPayment> endOfMonth =
                scheduledPaymentRepository.findForEndOfMonth(Instant.now());

        assertThat(endOfMonth.size()).isEqualTo(1);
        assertThat(endOfMonth.getFirst().getValidUntil()).isNull();
    }

    @Test
    void findAllByDayOfMonth_oneTransactionExists_findsCorrectTransaction() {
        List<ScheduledPayment> dayOfMonth =
                scheduledPaymentRepository.findForEndOfMonth(Instant.now());

        assertThat(dayOfMonth.size()).isEqualTo(1);
        assertThat(dayOfMonth.getFirst().getValidUntil()).isNull();
    }

    @Test
    void findAllByDayOfYear_oneTransactionExists_findsCorrectTransaction() {
        List<ScheduledPayment> dayOfMonth =
                scheduledPaymentRepository.findAllByDayOfYear(1, Instant.now());

        assertThat(dayOfMonth.size()).isEqualTo(1);
        assertThat(dayOfMonth.getFirst().getValidUntil()).isNull();
    }
}