package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "scheduled_payment")
public class ScheduledPayment {

    @Id
    private String id = UUID.randomUUID().toString();

    private BigDecimal amount;

    private Integer dayOfWeek;
    /**
     * From george: "if you schedule after 28th of the month it will always be executed at the last day of the month"
     */
    private Integer dayOfMonth;

    private Integer dayOfYear;

    private Instant validUntil;

    @ManyToOne
    private AccountDbo withdrawsFrom;

    @ManyToOne
    private AccountDbo depositsTo;
}
