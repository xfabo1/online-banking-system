package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "transactions")
public class TransactionDbo {
	@Builder.Default
	@Column(name = "transaction_time", updatable = false, nullable = false)
	private final Instant transactionTime = Instant.now();
	@Column(name = "conversion_rate")
	private Double conversionRate;
	@ManyToOne
	@JoinColumn(name = "withdraws_from", nullable = false)
	private AccountDbo withdrawsFrom;
	@ManyToOne
	@JoinColumn(name = "deposits_to", nullable = false)
	private AccountDbo depositsTo;
	@Column(name = "withdrawn_amount", nullable = false)
	private BigDecimal withdrawAmount;
	@Column(name = "deposited_amount")
	private BigDecimal depositAmount;
	@Column(name = "note")
	private String note;
	@Column(name = "variable_symbol")
	private String variableSymbol;
	@Builder.Default
	@Id
	private String id = UUID.randomUUID().toString();
	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_state", nullable = false)
	private TransactionState transactionState = TransactionState.PENDING;
}
