package cz.muni.fi.obs.etl.step.create.facts;

import cz.muni.fi.obs.data.AccountRepository;
import cz.muni.fi.obs.data.AnalyticsRepository;
import cz.muni.fi.obs.data.DateRepository;
import cz.muni.fi.obs.data.dbo.*;
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
    private final TransactionClient transactionClient;
    private final AccountRepository accountRepository;
    private final DateRepository dateRepository;
    private final LocalDate currentDate = LocalDate.now();

    @Autowired
    public FactCreatorProcessor(TransactionClient transactionClient, AnalyticsRepository analyticsRepository, AccountRepository accountRepository, DateRepository dateRepository) {
        this.transactionClient = transactionClient;
        this.accountRepository = accountRepository;
        this.dateRepository = dateRepository;
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

        AccountDimension accountDimension = findOrCreateAccountDimension(tempAccount);
        DateDimension dateDimension = findOrCreateDateDimension();
        CurrencyDimension currencyDimension = findOrCreateCurrencyDimension();


        DailyTransactionFact dailyTransactionFact = new DailyTransactionFact();
        dailyTransactionFact.setTotalWithdrawalTransactions(totalWithdrawalTransactions);
        dailyTransactionFact.setTotalDepositTransactions(totalDepositTransactions);
        dailyTransactionFact.setTotalTransactionAmount(totalTransactionAmount);
        dailyTransactionFact.setTotalWithdrawalAmount(totalWithdrawalAmount);
        dailyTransactionFact.setTotalDepositAmount(totalDepositAmount);
        dailyTransactionFact.setAverageWithdrawalAmount(averageWithdrawalAmount);
        dailyTransactionFact.setAverageDepositAmount(averageDepositAmount);
        dailyTransactionFact.setAccountDimension(accountDimension);
        dailyTransactionFact.setDateDimension(dateDimension);

        dailyTransactionFact.setCurrencyDimension(currencyDimension);

        return dailyTransactionFact;
    }

    private CurrencyDimension findOrCreateCurrencyDimension() {
        return null;
    }

    private DateDimension findOrCreateDateDimension() {
        return dateRepository.findByYearAndMonthAndDay(currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth()).orElseGet(() ->
                dateRepository.save(new DateDimension(currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth())));
    }

    private AccountDimension findOrCreateAccountDimension(TempAccount tempAccount) {
        return accountRepository.findById(tempAccount.getId()).orElseGet(() ->
                accountRepository.save(new AccountDimension(tempAccount.getAccountNumber())));
    }
}
