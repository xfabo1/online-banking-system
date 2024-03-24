package cz.muni.fi.obs.api;

import lombok.Builder;

@Builder
public record CurrencyExchangeResult(
		String symbolFrom,
		String symbolTo,
		long exchangedRate,
		long sourceAmount,
		long exchangedAmount) {
}
