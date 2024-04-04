package cz.muni.fi.obs.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
public record CurrencyExchangeRequest(@NotBlank String from, @NotBlank String to, @NotNull @Min(0) BigDecimal amount) {
}
