package cz.muni.fi.obs.http;

import cz.muni.fi.obs.config.FeignClientConfiguration;
import cz.muni.fi.obs.http.api.TSAccount;
import cz.muni.fi.obs.http.api.TSAccountCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(
        name = "transaction-service",
        url = "${clients.transaction-service.url}",
        configuration = FeignClientConfiguration.TransactionServiceClientConfiguration.class,
        fallback = TransactionServiceClient.Fallback.class
)
public interface TransactionServiceClient {


    @PostMapping("/v1/accounts/create")
    TSAccount createAccount(TSAccountCreate tsAccountCreate);

    @GetMapping("/v1/accounts/customer/{customerId}")
    List<TSAccount> getAccountsByCustomerId(@PathVariable("customerId") String customerId);

    @Slf4j
    class Fallback implements TransactionServiceClient {

        @Override
        public TSAccount createAccount(TSAccountCreate currencyExchangeRequest) {
            log.error("Could not create account, returning null");
            return null;
        }

        @Override
        public List<TSAccount> getAccountsByCustomerId(String customerId) {
            log.error("Could not get accounts by customer id, returning null");
            return null;
        }
    }
}
