package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "daily_transaction")
public class DailyTransactionFact extends Dbo {

    @Column(nullable = false)
    Integer totalWithdrawalTransactions;

    @Column(nullable = false)
    Integer totalDepositTransactions;

    @Column(nullable = false)
    BigDecimal totalTransactionAmount;

    @Column(nullable = false)
    BigDecimal totalWithdrawalAmount;

    @Column(nullable = false)
    BigDecimal totalDepositAmount;

    @Column(nullable = false)
    BigDecimal averageWithdrawalAmount;

    @Column(nullable = false)
    BigDecimal averageDepositAmount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    AccountDimension accountDimension;

    @ManyToOne
    @JoinColumn(name = "date_id")
    DateDimension dateDimension;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    CurrencyDimension currencyDimension;
}
