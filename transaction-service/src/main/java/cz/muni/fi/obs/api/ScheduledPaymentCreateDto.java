package cz.muni.fi.obs.api;

import cz.muni.fi.obs.data.dbo.PaymentFrequency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record ScheduledPaymentCreateDto(@NotNull LocalDate executeDate,
                                        Instant validUntil,
                                        @NotNull @Min(0) BigDecimal amount,
                                        @NotNull PaymentFrequency frequency,
                                        @NotNull String withdrawsFromId,
                                        @NotNull String depositsToId) {
}
