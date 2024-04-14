package cz.muni.fi.obs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TransactionManagement {

    public static void main(String[] args) {
        SpringApplication.run(TransactionManagement.class, args);
    }
}
