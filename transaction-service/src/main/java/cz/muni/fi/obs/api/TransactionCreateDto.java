package cz.muni.fi.obs.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TransactionCreateDto(
		@NotBlank
		String withdrawsFrom,
		@NotBlank
		String depositsTo,
		@Min(0)long withdrawAmount,
		@Min(0)long depositAmount,
		String note,
		@NotBlank
		String variableSymbol,
		@NotBlank
		String state) {
}
