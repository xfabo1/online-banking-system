package cz.muni.fi.obs.etl.step.read.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.obs.data.dbo.TempAccount;
import cz.muni.fi.obs.etl.dto.AccountDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Component
@StepScope
public class AccountProcessor implements ItemProcessor<AccountDto, TempAccount> {

    @Override
    public TempAccount process(AccountDto item) {
        return new TempAccount(
                item.getCustomerId(),
                item.getCurrencyCode(),
                item.getAccountNumber()
        );
    }
}
