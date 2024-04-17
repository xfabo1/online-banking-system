package cz.muni.fi.obs.data.dbo;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import lombok.Builder;

@Builder
public record AccountDbo(
		@Id String id,
		String customerId,
		String currencyCode,
		String accountNumber) {
}
