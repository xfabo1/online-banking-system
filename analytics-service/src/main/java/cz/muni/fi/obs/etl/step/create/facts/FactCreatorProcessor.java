package cz.muni.fi.obs.etl.step.create.facts;

import cz.muni.fi.obs.data.dbo.DailyTransactionFact;
import cz.muni.fi.obs.data.dbo.TempAccount;
import cz.muni.fi.obs.etl.clients.TransactionClient;
import cz.muni.fi.obs.etl.dto.TransactionDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * TODO: use transaction client to fetch transactions for current day for the account,
 * do all computations over these within the method and create the fact
 */

@Component
@StepScope
public class FactCreatorProcessor implements ItemProcessor<TempAccount, DailyTransactionFact> {
    private TransactionClient transactionClient;

    @Autowired
    public FactCreatorProcessor(TransactionClient transactionClient) {
        this.transactionClient = transactionClient;
    }

    @Override
    public DailyTransactionFact process(TempAccount tempAccount) throws Exception {
        Page<TransactionDto> transactions = transactionClient.listTransactions(tempAccount.getAccountNumber(), LocalDate.now());

        int totalWithdrawalTransactions = 0;
        int totalDepositTransactions = 0;
        BigDecimal totalTransactionAmount = BigDecimal.ZERO;
        BigDecimal totalWithdrawalAmount = BigDecimal.ZERO;
        BigDecimal totalDepositAmount = BigDecimal.ZERO;
        BigDecimal averageWithdrawalAmount = BigDecimal.ZERO;
        BigDecimal averageDepositAmount = BigDecimal.ZERO;

        List<TransactionDto> withdrawalTransactions = transactions.stream()
                .filter(transaction -> transaction.getWithdrawsFromAccountNumber().equals(tempAccount.getAccountNumber()))
                .toList();
        List<TransactionDto> depositTransactions = transactions.stream()
                .filter(transaction -> transaction.getDepositsToAccountNumber().equals(tempAccount.getAccountNumber()))
                .toList();

        for (TransactionDto transaction : withdrawalTransactions) {
            totalWithdrawalTransactions++;
            totalTransactionAmount = totalTransactionAmount.add(transaction.getWithdrawAmount());
            totalWithdrawalAmount = totalWithdrawalAmount.add(transaction.getWithdrawAmount());
        }

        for (TransactionDto transaction : depositTransactions) {
            totalDepositTransactions++;
            totalTransactionAmount = totalTransactionAmount.add(transaction.getDepositAmount());
            totalDepositAmount = totalDepositAmount.add(transaction.getDepositAmount());
        }

        if (totalWithdrawalTransactions > 0) {
            averageWithdrawalAmount = totalWithdrawalAmount.divide(BigDecimal.valueOf(totalWithdrawalTransactions), RoundingMode.HALF_UP);
        }

        if (totalDepositTransactions > 0) {
            averageDepositAmount = totalDepositAmount.divide(BigDecimal.valueOf(totalDepositTransactions), RoundingMode.HALF_UP);
        }


        DailyTransactionFact dailyTransactionFact = new DailyTransactionFact();
        dailyTransactionFact.setTotalWithdrawalTransactions(totalWithdrawalTransactions);
        dailyTransactionFact.setTotalDepositTransactions(totalDepositTransactions);
        dailyTransactionFact.setTotalTransactionAmount(totalTransactionAmount);
        dailyTransactionFact.setTotalWithdrawalAmount(totalWithdrawalAmount);
        dailyTransactionFact.setTotalDepositAmount(totalDepositAmount);
        dailyTransactionFact.setAverageWithdrawalAmount(averageWithdrawalAmount);
        dailyTransactionFact.setAverageDepositAmount(averageDepositAmount);
        dailyTransactionFact.setAccountDimension(null); // TODO implement
        dailyTransactionFact.setDateDimension(null); // TODO Implement

        return dailyTransactionFact;
    }
}
