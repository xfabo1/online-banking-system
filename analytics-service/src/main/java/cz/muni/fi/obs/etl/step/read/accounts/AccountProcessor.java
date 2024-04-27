package cz.muni.fi.obs.etl.step.read.accounts;

import cz.muni.fi.obs.data.dbo.TempAccount;
import cz.muni.fi.obs.etl.dto.AccountDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * TODO: use a mapper to convert the DTO from API into the object stored in temp db
 */
@Component
@StepScope
public class AccountProcessor implements ItemProcessor<AccountDto, TempAccount> {

    @Override
    public TempAccount process(AccountDto item) throws Exception {
        return null;
    }
}
