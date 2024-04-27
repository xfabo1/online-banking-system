package cz.muni.fi.obs.etl.step.read.accounts;

import cz.muni.fi.obs.data.dbo.TempAccount;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * TODO: use repository to write accounts
 */
@Component
@StepScope
public class AccountWriter implements ItemWriter<TempAccount> {

    @Override
    public void write(Chunk<? extends TempAccount> chunk) throws Exception {

    }
}
