package cz.muni.fi.obs.api;

import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Validated
public record CurrencyExchangeRequest(@NotBlank String from, @NotBlank String to, @NotNull @Min(0) BigDecimal amount) {
}
