package cz.muni.fi.obs.etl.step.create.facts;

import cz.muni.fi.obs.data.AnalyticsRepository;
import cz.muni.fi.obs.data.dbo.DailyTransactionFact;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@StepScope
public class FactWriter implements ItemWriter<DailyTransactionFact> {
    private final AnalyticsRepository analyticsRepository;

    @Autowired
    public FactWriter(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @Override
    public void write(Chunk<? extends DailyTransactionFact> chunk) throws Exception {
        analyticsRepository.saveAll(chunk.getItems());
    }
}
