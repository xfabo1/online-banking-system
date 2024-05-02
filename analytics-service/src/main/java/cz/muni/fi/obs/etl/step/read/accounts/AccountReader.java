package cz.muni.fi.obs.etl.step.read.accounts;

import cz.muni.fi.obs.etl.clients.AccountsClient;
import cz.muni.fi.obs.etl.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
@StepScope
@Slf4j
public class AccountReader implements ItemReader<AccountDto> {

    private final AccountsClient accountsClient;

    @Autowired
    public AccountReader(AccountsClient accountsClient) {
        this.accountsClient = accountsClient;
    }

    private Page<AccountDto> currentPage;
    private int currentPageNumber = 0;

    @Override
    public AccountDto read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
        if (currentPage == null || currentPageNumber >= currentPage.getTotalPages()) {
            currentPage = fetchNextPage();
            currentPageNumber = 0;
        }
        if (currentPage != null && currentPage.hasContent()) {
            return currentPage.getContent().get(currentPageNumber++);
        }
        return null;
    }

    private Page<AccountDto> fetchNextPage() {
        try {
            Pageable pageable = PageRequest.of(currentPageNumber, 10);
            return accountsClient.listAccounts(pageable);
        } catch (Exception e) {
            log.error("Error fetching accounts from transaction service: {}", e.getMessage());
            return null;
        }
    }

}
