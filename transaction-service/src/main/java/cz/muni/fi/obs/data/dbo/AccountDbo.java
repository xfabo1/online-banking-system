package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "accounts")
public class AccountDbo {

	@Id
	private String id;
	@Column(name = "customer_id", nullable = false)
	private String customerId;
	@Column(name = "currency_code", nullable = false)
	private String currencyCode;

	@Builder.Default
	@Column(name = "bank_account", nullable = false)
	private boolean isBankAccount = false;
}
