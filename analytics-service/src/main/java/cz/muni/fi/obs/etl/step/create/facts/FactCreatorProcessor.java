package cz.muni.fi.obs.etl.step.create.facts;

import cz.muni.fi.obs.data.dbo.DailyTransaction;
import cz.muni.fi.obs.data.dbo.TempAccount;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * TODO: use transaction client to fetch transactions for current day for the account,
 * do all computations over these within the method and create the fact {@link DailyTransaction}
 */
@Component
@StepScope
public class FactCreatorProcessor implements ItemProcessor<TempAccount, DailyTransaction> {

    @Override
    public DailyTransaction process(TempAccount item) throws Exception {
        return null;
    }
}
