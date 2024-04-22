package cz.muni.fi.obs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
@EnableTransactionManagement
public class CurrencyServiceApplication {

    public static void main(String[] args) {
         SpringApplication.run(CurrencyServiceApplication.class, args);
    }
}
