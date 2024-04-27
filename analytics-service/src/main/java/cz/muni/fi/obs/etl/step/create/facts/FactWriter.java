package cz.muni.fi.obs.etl.step.create.facts;

import cz.muni.fi.obs.data.dbo.DailyTransaction;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * TODO: just write the chunk to DB nothing else needed
 */
@Component
@StepScope
public class FactWriter implements ItemWriter<DailyTransaction> {
    @Override
    public void write(Chunk<? extends DailyTransaction> chunk) throws Exception {

    }
}
