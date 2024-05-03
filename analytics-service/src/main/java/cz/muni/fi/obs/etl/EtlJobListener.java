package cz.muni.fi.obs.etl;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EtlJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(@NotNull JobExecution jobExecution) {
        JobExecutionListener.super.beforeJob(jobExecution);
        log.info("Starting job: {} at {}", jobExecution.getJobInstance().getJobName(), jobExecution.getStartTime());
    }

    @Override
    public void afterJob(@NotNull JobExecution jobExecution) {
        JobExecutionListener.super.afterJob(jobExecution);
        log.info("Job finished with status: {} at {}", jobExecution.getStatus(), jobExecution.getEndTime());
    }
}
