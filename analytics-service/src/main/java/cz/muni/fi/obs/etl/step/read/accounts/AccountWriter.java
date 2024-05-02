package cz.muni.fi.obs.etl.step.read.accounts;

import cz.muni.fi.obs.data.dbo.TempAccount;
import cz.muni.fi.obs.etl.step.clean.accounts.TempAccountRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@StepScope
public class AccountWriter implements ItemWriter<TempAccount> {
    private final TempAccountRepository tempAccountRepository;

    @Autowired
    public AccountWriter(TempAccountRepository tempAccountRepository) {
        this.tempAccountRepository = tempAccountRepository;
    }

    @Override
    public void write(Chunk<? extends TempAccount> chunk) throws Exception {
        tempAccountRepository.saveAll(chunk.getItems());
    }
}
