package cz.muni.fi.obs.data.dbo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
	@SequenceGenerator(name = "account_number_sequence", allocationSize = 1)
	@Column(name = "account_number", nullable = false, unique = true)
	@JsonIgnore
	private Integer accountNumber;

	@Transient
	public String getAccountNumber() {
		return String.format("%08d", accountNumber);
	}
}
