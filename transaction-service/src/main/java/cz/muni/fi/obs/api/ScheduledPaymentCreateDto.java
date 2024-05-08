package cz.muni.fi.obs.api;

import java.time.Instant;
import java.time.LocalDate;

import cz.muni.fi.obs.data.dbo.PaymentFrequency;
import jakarta.validation.constraints.NotNull;

public record ScheduledPaymentCreateDto(@NotNull LocalDate executeDate,
                                        Instant validUntil,
                                        @NotNull PaymentFrequency frequency,
                                        @NotNull String withdrawsFromId,
                                        @NotNull String depositsToId) {
}
