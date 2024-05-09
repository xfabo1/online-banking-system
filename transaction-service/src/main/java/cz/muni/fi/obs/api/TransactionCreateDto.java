package cz.muni.fi.obs.api;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TransactionCreateDto(
		@NotBlank
		String withdrawsFromAccount,
		@NotBlank
		String depositsToAccount,
		@Min(0)
		@NotNull BigDecimal withdrawAmount,
		String note,
		String variableSymbol) {
}
