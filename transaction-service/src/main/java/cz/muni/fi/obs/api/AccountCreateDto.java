package cz.muni.fi.obs.api;


public record AccountCreateDto(
		String customerId,
		String currencyCode,
		String accountNumber) {
}
