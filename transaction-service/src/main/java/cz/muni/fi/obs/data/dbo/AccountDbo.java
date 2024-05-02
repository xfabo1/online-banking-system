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
@EqualsAndHashCode(of = "id")
@Table(name = "accounts")
public class AccountDbo {

	@Id
	private String id;
	@Column(name = "customer_id", nullable = false)
	private String customerId;
	@Column(name = "currency_code", nullable = false)
	private String currencyCode;
}
