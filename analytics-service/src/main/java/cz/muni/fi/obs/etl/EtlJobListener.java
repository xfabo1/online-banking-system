package cz.muni.fi.obs.etl;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

/**
 * TODO: Check for status before and after start of job, log the start of job and end of job
 * optional: use the jobExecution object to log some details (how many facts were created in the compute-facts-step)
 */
@Component
public class EtlJobListener implements JobExecutionListener {


    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobExecutionListener.super.beforeJob(jobExecution);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        JobExecutionListener.super.afterJob(jobExecution);
    }
}
