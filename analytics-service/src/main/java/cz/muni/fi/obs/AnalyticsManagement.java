package cz.muni.fi.obs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AnalyticsManagement {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsManagement.class, args);
    }
}
