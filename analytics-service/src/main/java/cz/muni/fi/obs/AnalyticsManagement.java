package cz.muni.fi.obs;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients
@EnableScheduling
@EnableBatchProcessing
public class AnalyticsManagement {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsManagement.class, args);
    }
}
