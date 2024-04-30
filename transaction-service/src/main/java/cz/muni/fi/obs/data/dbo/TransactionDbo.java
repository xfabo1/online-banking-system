package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "transactions")
public class TransactionDbo {
	@Id
	private String id;
	@Column(name = "conversion_rate", nullable = false)
	private Double conversionRate;
	@ManyToOne
	@JoinColumn(name = "withdraws_from", nullable = false)
	private AccountDbo withdrawsFrom;
	@ManyToOne
	@JoinColumn(name = "deposits_to", nullable = false)
	private AccountDbo depositsTo;
	@Column(name = "withdrawn_amount", nullable = false)
	private BigDecimal withdrawAmount;
	@Column(name = "deposited_amount", nullable = false)
	private BigDecimal depositAmount;
	@Column(name = "note")
	private String note;
	@Column(name = "variable_symbol")
	private String variableSymbol;

	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_state", nullable = false)
	private TransactionState transactionState;
}
