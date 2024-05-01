package cz.muni.fi.obs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableJpaRepositories
@EnableJms
public class TransactionManagement {

    public static void main(String[] args) {
        SpringApplication.run(TransactionManagement.class, args);
    }
}
