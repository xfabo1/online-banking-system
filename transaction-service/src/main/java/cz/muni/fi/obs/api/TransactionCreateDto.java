package cz.muni.fi.obs.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionCreateDto(
		@NotBlank
		String withdrawsFromAccountNumber,
		@NotBlank
		String depositsToAccountNumber,
		@Min(0) BigDecimal withdrawAmount,
		@Min(0) BigDecimal depositAmount,
		String note,
		String variableSymbol) {
}
