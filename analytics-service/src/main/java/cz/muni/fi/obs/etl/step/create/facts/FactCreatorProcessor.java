package cz.muni.fi.obs.etl.step.create.facts;

import cz.muni.fi.obs.data.*;
import cz.muni.fi.obs.data.dbo.*;
import cz.muni.fi.obs.etl.clients.TransactionClient;
import cz.muni.fi.obs.etl.dto.TransactionDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@StepScope
public class FactCreatorProcessor implements ItemProcessor<TempAccount, DailyTransactionFact> {
    private final TransactionClient transactionClient;
    private final AccountRepository accountRepository;
    private final DateRepository dateRepository;
    private final CurrencyRepository currencyRepository;
    private final LocalDate currentDate = LocalDate.now();

    @Autowired
    public FactCreatorProcessor(TransactionClient transactionClient, AccountRepository accountRepository, DateRepository dateRepository, CurrencyRepository currencyRepository) {
        this.transactionClient = transactionClient;
        this.accountRepository = accountRepository;
        this.dateRepository = dateRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public DailyTransactionFact process(TempAccount tempAccount) throws Exception {
        List<TransactionDto> transactions = transactionClient.listTransactions(tempAccount.getAccountNumber(), LocalDate.now()).getContent();

        List<TransactionDto> withdrawalTransactions = transactions.stream()
                .filter(transaction -> transaction.getWithdrawsFromAccountNumber().equals(tempAccount.getAccountNumber()))
                .toList();

        List<TransactionDto> depositTransactions = transactions.stream()
                .filter(transaction -> transaction.getDepositsToAccountNumber().equals(tempAccount.getAccountNumber()))
                .toList();

        int totalWithdrawalTransactions = withdrawalTransactions.size();
        int totalDepositTransactions = depositTransactions.size();

        BigDecimal totalWithdrawalAmount = withdrawalTransactions.stream()
                .map(TransactionDto::getWithdrawAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDepositAmount = depositTransactions.stream()
                .map(TransactionDto::getDepositAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTransactionAmount = totalWithdrawalAmount.add(totalDepositAmount);

        BigDecimal averageWithdrawalAmount = totalWithdrawalTransactions > 0 ? totalWithdrawalAmount.divide(BigDecimal.valueOf(totalWithdrawalTransactions), RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal averageDepositAmount = totalDepositTransactions > 0 ? totalDepositAmount.divide(BigDecimal.valueOf(totalDepositTransactions), RoundingMode.HALF_UP) : BigDecimal.ZERO;

        AccountDimension accountDimension = findOrCreateAccountDimension(tempAccount);
        DateDimension dateDimension = findOrCreateDateDimension();
        CurrencyDimension currencyDimension = findOrCreateCurrencyDimension(tempAccount.getCurrencyCode());

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

    private CurrencyDimension findOrCreateCurrencyDimension(String currencyCode) {
        return currencyRepository.findByCurrencyCode(currencyCode).orElseGet(() ->
                currencyRepository.save(new CurrencyDimension(currencyCode)));
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