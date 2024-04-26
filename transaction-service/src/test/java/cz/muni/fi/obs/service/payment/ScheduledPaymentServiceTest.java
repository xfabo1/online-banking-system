package cz.muni.fi.obs.service.payment;

import cz.muni.fi.obs.api.ScheduledPaymentCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.PaymentFrequency;
import cz.muni.fi.obs.data.dbo.ScheduledPayment;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.data.repository.ScheduledPaymentRepository;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class ScheduledPaymentServiceTest {

    @Autowired
    private ScheduledPaymentService scheduledPaymentService;

    @Autowired
    private ScheduledPaymentRepository repository;

    @Autowired
    private AccountRepository accountRepository;

    private boolean setupDone = false;

    @BeforeEach
    public void setUp() {
        if (setupDone) {
            return;
        }

        AccountDbo accountDbo = new AccountDbo();
        accountDbo.setId("123");
        accountDbo.setAccountNumber("1233");
        accountDbo.setCurrencyCode("CZK");
        accountDbo.setCustomerId("mikoflosso");

        accountRepository.save(accountDbo);

        AccountDbo accountDbo1 = new AccountDbo();
        accountDbo1.setId("1234");
        accountDbo1.setAccountNumber("12356");
        accountDbo1.setCurrencyCode("CZK");
        accountDbo1.setCustomerId("ego");

        accountRepository.save(accountDbo1);

        setupDone = true;
    }

    @AfterEach
    public void cleanScheduledPayments() {
        repository.deleteAll();
    }

    @Test
    public void createScheduledPaymentMonthly_bothAccountsExist_createsPaymentMonthly() {
        LocalDate date = LocalDate.now();
        ScheduledPaymentCreateDto scheduledPaymentCreateDto = new ScheduledPaymentCreateDto(date,
                null, PaymentFrequency.MONTHLY, "123", "1234");

        scheduledPaymentService.createPayment(scheduledPaymentCreateDto);

        List<ScheduledPayment> allPayments = repository.findAll();
        assertThat(allPayments.size()).isEqualTo(1);

        ScheduledPayment first = allPayments.getFirst();

        assertThat(first.getDayOfMonth()).isEqualTo(date.getDayOfMonth());
        assertThat(first.getDayOfWeek()).isNull();
        assertThat(first.getDayOfYear()).isNull();
    }

    @Test
    public void createScheduledPaymentYearly_bothAccountsExist_createsPaymentYearly() {
        LocalDate date = LocalDate.now();
        ScheduledPaymentCreateDto scheduledPaymentCreateDto = new ScheduledPaymentCreateDto(date,
                null, PaymentFrequency.YEARLY, "123", "1234");

        scheduledPaymentService.createPayment(scheduledPaymentCreateDto);

        List<ScheduledPayment> allPayments = repository.findAll();
        assertThat(allPayments.size()).isEqualTo(1);

        ScheduledPayment first = allPayments.getFirst();

        assertThat(first.getDayOfMonth()).isNull();
        assertThat(first.getDayOfWeek()).isNull();
        assertThat(first.getDayOfYear()).isEqualTo(date.getDayOfYear());
    }

    @Test
    public void createScheduledPaymentWeekly_bothAccountsExists_createsWeeklyPayment() {
        LocalDate date = LocalDate.now();
        ScheduledPaymentCreateDto scheduledPaymentCreateDto = new ScheduledPaymentCreateDto(date,
                null, PaymentFrequency.WEEKLY, "123", "1234");

        scheduledPaymentService.createPayment(scheduledPaymentCreateDto);

        List<ScheduledPayment> allPayments = repository.findAll();
        assertThat(allPayments.size()).isEqualTo(1);

        ScheduledPayment first = allPayments.getFirst();

        assertThat(first.getDayOfMonth()).isNull();
        assertThat(first.getDayOfWeek()).isEqualTo(date.getDayOfWeek().ordinal());
        assertThat(first.getDayOfYear()).isNull();
    }

    @Test
    public void createScheduledPayment_toAccountDoesNotExist_throwsException() {
        ScheduledPaymentCreateDto scheduledPaymentCreateDto = new ScheduledPaymentCreateDto(LocalDate.now(),
                null, PaymentFrequency.WEEKLY, "this id does not exist in db", "1234");

        assertThrows(ResourceNotFoundException.class, () -> scheduledPaymentService.createPayment(scheduledPaymentCreateDto));
    }

    @Test
    public void disableScheduledPayment_paymentExists_disablesPayment() {
        LocalDate date = LocalDate.now();
        ScheduledPaymentCreateDto scheduledPaymentCreateDto = new ScheduledPaymentCreateDto(date,
                null, PaymentFrequency.WEEKLY, "123", "1234");

        scheduledPaymentService.createPayment(scheduledPaymentCreateDto);

        ScheduledPayment payment = repository.findAll().getFirst();
        scheduledPaymentService.disablePayment(payment.getId(), Instant.now());

        ScheduledPayment retrieved = repository.findById(payment.getId())
                .orElseThrow(() -> new IllegalStateException("Payment was created but does not exist anymore"));

        assertThat(retrieved.getValidUntil()).isBefore(Instant.now());
    }

    @Test
    public void disableScheduledPayment_paymentDoesNotExist_disablesPayment() {
        assertThrows(ResourceNotFoundException.class,
                () -> scheduledPaymentService.disablePayment("123", Instant.now()));
    }
}