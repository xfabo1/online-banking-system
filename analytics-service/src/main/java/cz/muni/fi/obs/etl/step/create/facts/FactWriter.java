package cz.muni.fi.obs.etl.step.create.facts;

import cz.muni.fi.obs.data.dbo.DailyTransactionFact;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * TODO: just write the chunk to DB nothing else needed
 */
@Component
@StepScope
public class FactWriter implements ItemWriter<DailyTransactionFact> {
    @Override
    public void write(Chunk<? extends DailyTransactionFact> chunk) throws Exception {

    }
}
