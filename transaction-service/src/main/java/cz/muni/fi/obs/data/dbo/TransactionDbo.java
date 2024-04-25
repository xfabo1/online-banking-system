package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.*;
import lombok.*;

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
	private TransactionState transactionState;
}
