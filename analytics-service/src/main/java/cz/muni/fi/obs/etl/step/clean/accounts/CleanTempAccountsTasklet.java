package cz.muni.fi.obs.etl.step.clean.accounts;

import cz.muni.fi.obs.data.repository.TempAccountRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@StepScope
public class CleanTempAccountsTasklet implements Tasklet {

    private final TempAccountRepository tempAccountRepository;

    @Autowired
    public CleanTempAccountsTasklet(TempAccountRepository tempAccountRepository) {
        this.tempAccountRepository = tempAccountRepository;
    }


    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        tempAccountRepository.deleteAll();
        return RepeatStatus.FINISHED;
    }
}
