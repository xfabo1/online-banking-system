package cz.muni.fi.obs.data.dbo;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;

import lombok.Builder;

@Builder
public record TransactionDbo(
		@Id String id,
		Double conversionRate,
		String withdrawsFrom,
		String depositsTo,
		BigDecimal withdrawAmount,
		BigDecimal depositAmount,
		String note,
		String variableSymbol) {
}
