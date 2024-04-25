package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.*;
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


    private Integer dayOfWeek;

    private BigDecimal amount;

    /**
     * From george: "if you schedule after 28th of the month it will always be executed at the last day of the month"
     */
    private Integer dayOfMonth;

    private Integer month;

    private Integer dayOfYear;

    private Instant validUntil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentFrequency frequency;

    @ManyToOne
    private AccountDbo withdrawsFrom;

    @ManyToOne
    private AccountDbo depositsTo;
}
