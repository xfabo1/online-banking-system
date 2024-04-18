package cz.muni.fi.obs.data.dbo;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "transactions")
public class TransactionDbo {
	@Id
	private String id;
	@Column(name = "conversion_rate", nullable = false)
	private Double conversionRate;
	@ManyToOne()
	@JoinColumn(name = "withdraws_from", nullable = false)
	private AccountDbo withdrawsFrom;
	@ManyToOne()
	@JoinColumn(name = "deposits_to", nullable = false)
	private AccountDbo depositsTo;
	@Column(name = "withdraw_amount", nullable = false)
	private BigDecimal withdrawAmount;
	@Column(name = "deposit_amount", nullable = false)
	private BigDecimal depositAmount;
	@Column(name = "note")
	private String note;
	@Column(name = "variable_symbol")
	private String variableSymbol;
}
