package cz.muni.fi.obs.etl.step.create.facts;

import cz.muni.fi.obs.data.dbo.TempAccount;
import cz.muni.fi.obs.etl.step.clean.accounts.TempAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class TempAccountReader implements ItemReader<TempAccount> {

    private final TempAccountRepository tempAccountRepository;

    @Autowired
    public TempAccountReader(TempAccountRepository tempAccountRepository) {
        this.tempAccountRepository = tempAccountRepository;
    }

    private Page<TempAccount> currentPage;
    private int currentPageNumber = 0;
    private int currentPageItem = 0;
    private int totalPages;

    @Override
    public TempAccount read() {
        if (currentPage == null) {
            currentPage = fetchNextPage();
            totalPages = currentPage.getTotalPages();
        }
        if (currentPageItem < currentPage.getSize()) {
            return getAndIncrement();
        } else if (currentPageNumber < totalPages) {
            currentPageItem = 0;
            currentPageNumber += 1;
            currentPage = fetchNextPage();
            return getAndIncrement();
        } else {
            return null;
        }
    }

    private TempAccount getAndIncrement() {
        TempAccount tempAccount = currentPage.getContent().get(currentPageItem);
        currentPageItem += 1;
        return tempAccount;
    }

    private Page<TempAccount> fetchNextPage() {
        try {
            Pageable pageable = PageRequest.of(currentPageNumber, 10);
            return tempAccountRepository.findAll(pageable);
        } catch (Exception e) {
            log.error("Failed to fetch page", e);
            throw e;
        }
    }
}
