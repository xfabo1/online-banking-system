package cz.muni.fi.obs.etl;

import cz.muni.fi.obs.data.dbo.DailyTransaction;
import cz.muni.fi.obs.data.dbo.TempAccount;
import cz.muni.fi.obs.etl.dto.AccountDto;
import cz.muni.fi.obs.etl.step.clean.accounts.CleanTempAccountsTasklet;
import cz.muni.fi.obs.etl.step.create.facts.FactCreatorProcessor;
import cz.muni.fi.obs.etl.step.create.facts.FactWriter;
import cz.muni.fi.obs.etl.step.create.facts.TempAccountReader;
import cz.muni.fi.obs.etl.step.read.accounts.AccountProcessor;
import cz.muni.fi.obs.etl.step.read.accounts.AccountReader;
import cz.muni.fi.obs.etl.step.read.accounts.AccountWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfiguration {

    public static final int CHUNK_SIZE = 10;

    public static final String READ_ACCOUNTS_STEP_NAME = "read-accounts-step";

    public static final String CREATE_FACTS_STEP_NAME = "create-facts-step";

    public static final String CLEAN_ACCOUNTS_STEP_NAME = "clean-accounts-step";

    public static final String JOB_NAME = "daily-etl-job";

    @Bean
    public Step readAccountsStep(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager,
                                 AccountReader accountReader,
                                 AccountProcessor accountProcessor,
                                 AccountWriter accountWriter) {
        return new StepBuilder(READ_ACCOUNTS_STEP_NAME, jobRepository)
                .<AccountDto, TempAccount>chunk(CHUNK_SIZE, transactionManager)
                .reader(accountReader)
                .processor(accountProcessor)
                .writer(accountWriter)
                .build();
    }

    @Bean
    public Step computeAccountFactsStep(JobRepository jobRepository,
                                        PlatformTransactionManager transactionManager,
                                        TempAccountReader reader,
                                        FactCreatorProcessor processor,
                                        FactWriter writer) {
        return new StepBuilder(CREATE_FACTS_STEP_NAME, jobRepository)
                .<TempAccount, DailyTransaction>chunk(CHUNK_SIZE, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step cleanAccountsStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  CleanTempAccountsTasklet tasklet) {
        return new StepBuilder(CLEAN_ACCOUNTS_STEP_NAME, jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    public Job etlJob(JobRepository jobRepository,
                      EtlJobListener jobListener,
                      Step cleanAccountsStep,
                      Step readAccountsStep,
                      Step computeAccountFactsStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .listener(jobListener)
                .start(cleanAccountsStep)
                .next(readAccountsStep)
                .next(computeAccountFactsStep)
                .build();
    }
}
