package cz.muni.fi.obs.api;


import jakarta.validation.constraints.NotBlank;


public record AccountCreateDto(
		@NotBlank
		String customerId,
		@NotBlank
		String currencyCode,
		@NotBlank
		String accountNumber) {
}
