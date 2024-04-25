package cz.muni.fi.obs.api;

import cz.muni.fi.obs.data.dbo.PaymentFrequency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class ScheduledPaymentDto {

    private Integer dayOfWeek;

    private BigDecimal amount;

    /**
     * From george: "if you schedule after 28th of the month it will always be executed at the last day of the month"
     */
    private Integer dayOfMonth;

    private Integer month;

    private Integer dayOfYear;

    private Instant validUntil;

    private PaymentFrequency frequency;

}
