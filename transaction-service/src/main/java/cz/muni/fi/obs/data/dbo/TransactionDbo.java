package cz.muni.fi.obs.data.dbo;

import org.springframework.data.annotation.Id;

import lombok.Builder;

@Builder
public record TransactionDbo(
		@Id String id,
		long conversionRate,
		String withdrawsFrom,
		String depositsTo,
		long withdrawAmount,
		long depositAmount,
		String note,
		String variableSymbol,
		String state) {
}
