package cz.muni.fi.obs.etl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;

@Service
@Slf4j
public class EtlExecutor {

    private final JobLauncher jobLauncher;

    private final Job etlJob;

    @Autowired
    public EtlExecutor(JobLauncher jobLauncher, Job etlJob) {
        this.jobLauncher = jobLauncher;
        this.etlJob = etlJob;
    }

    /**
     * Scheduled 1 am every day
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void executeEtl() {
        JobParameter<String> timeParameter = new JobParameter<>(Instant.now().toString(), String.class, true);
        HashMap<String, JobParameter<?>> parameterMap = new HashMap<>();
        parameterMap.put("job-execution-time", timeParameter);
        JobParameters jobParameters = new JobParameters(parameterMap);

        log.info("Starting etl...");

        try {
            jobLauncher.run(etlJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new EtlException(e);
        }
    }
}
