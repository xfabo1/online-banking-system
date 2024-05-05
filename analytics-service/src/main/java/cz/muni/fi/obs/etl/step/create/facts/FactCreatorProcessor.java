package cz.muni.fi.obs.etl.step.create.facts;

import cz.muni.fi.obs.data.dbo.AccountDimension;
import cz.muni.fi.obs.data.dbo.CurrencyDimension;
import cz.muni.fi.obs.data.dbo.DailyTransactionFact;
import cz.muni.fi.obs.data.dbo.DateDimension;
import cz.muni.fi.obs.data.dbo.TempAccount;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.data.repository.CurrencyRepository;
import cz.muni.fi.obs.data.repository.DateRepository;
import cz.muni.fi.obs.etl.EtlException;
import cz.muni.fi.obs.etl.clients.TransactionClient;
import cz.muni.fi.obs.etl.dto.TransactionDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@StepScope
public class FactCreatorProcessor implements ItemProcessor<TempAccount, DailyTransactionFact> {
    private final TransactionClient transactionClient;
    private final AccountRepository accountRepository;
    private final DateRepository dateRepository;
    private final CurrencyRepository currencyRepository;
    private final LocalDate currentDate = LocalDate.now();

    private Integer currentPage = 0;

    private Integer pageCount = 0;

    @Autowired
    public FactCreatorProcessor(TransactionClient transactionClient, AccountRepository accountRepository, DateRepository dateRepository, CurrencyRepository currencyRepository) {
        this.transactionClient = transactionClient;
        this.accountRepository = accountRepository;
        this.dateRepository = dateRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public DailyTransactionFact process(TempAccount tempAccount) {
        List<TransactionDto> transactions = fetchTransactions(tempAccount);

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

        BigDecimal averageWithdrawalAmount = totalWithdrawalTransactions > 0 ?
                totalWithdrawalAmount.divide(BigDecimal.valueOf(totalWithdrawalTransactions), RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal averageDepositAmount = totalDepositTransactions > 0 ?
                totalDepositAmount.divide(BigDecimal.valueOf(totalDepositTransactions), RoundingMode.HALF_UP) : BigDecimal.ZERO;

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

    private List<TransactionDto> fetchTransactions(TempAccount tempAccount) {
        List<TransactionDto> transactionDtos = new ArrayList<>();

        do {
            transactionDtos.addAll(doFetch(tempAccount));
        } while (currentPage < pageCount);

        return transactionDtos;
    }

    private List<TransactionDto> doFetch(TempAccount tempAccount) {
        ResponseEntity<Page<TransactionDto>> response = transactionClient.listTransactions(tempAccount.getId(),
                currentPage, 50, currentDate);

        if (!response.getStatusCode().equals(HttpStatusCode.valueOf(200)) || response.getBody() == null) {
            throw new EtlException(new RuntimeException("Transaction client failed to list transactions."));
        }

        Page<TransactionDto> page = response.getBody();
        if (pageCount == 0) {
            pageCount = page.getTotalPages();
        }

        currentPage += 1;

        return response.getBody().getContent();
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