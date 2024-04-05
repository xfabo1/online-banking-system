package cz.muni.fi.obs.data.dbo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class DailyTransaction extends Dbo {
    Integer totalWithdrawalTransactions;
    Integer totalDepositTransactions;
    BigDecimal totalTransactionAmount;
    BigDecimal totalWithdrawalAmount;
    BigDecimal totalDepositAmount;
    BigDecimal averageWithdrawalAmount;
    BigDecimal averageDepositAmount;
    Account account;
    Date date;


    public DailyTransaction(Integer totalWithdrawalTransactions, Integer totalDepositTransactions, BigDecimal totalTransactionAmount, BigDecimal totalWithdrawalAmount, BigDecimal totalDepositAmount, BigDecimal averageWithdrawalAmount, BigDecimal averageDepositAmount, Account account, Date date) {
        this.totalWithdrawalTransactions = totalWithdrawalTransactions;
        this.totalDepositTransactions = totalDepositTransactions;
        this.totalTransactionAmount = totalTransactionAmount;
        this.totalWithdrawalAmount = totalWithdrawalAmount;
        this.totalDepositAmount = totalDepositAmount;
        this.averageWithdrawalAmount = averageWithdrawalAmount;
        this.averageDepositAmount = averageDepositAmount;
        this.account = account;
        this.date = date;
    }
}
