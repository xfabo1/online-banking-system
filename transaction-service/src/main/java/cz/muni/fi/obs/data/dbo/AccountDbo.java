package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "accounts")
public class AccountDbo {

	@Id
	private String id;
	@Column(name = "customer_id", nullable = false, unique = true)
	private String customerId;
	@Column(name = "currency_code", nullable = false)
	private String currencyCode;
	@Column(name = "account_number", nullable = false, unique = true)
	private String accountNumber;
}
