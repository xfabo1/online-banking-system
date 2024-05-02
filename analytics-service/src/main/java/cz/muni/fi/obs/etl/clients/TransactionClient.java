package cz.muni.fi.obs.etl.clients;

import cz.muni.fi.obs.etl.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;


@FeignClient(
        name = "transaction-client",
        url = "${etl.transaction-service.url}"
)
public interface TransactionClient {
    @GetMapping(value = "/account/{accountId}")
    Page<TransactionDto> listTransactions(@PathVariable("accountId") String accountId, LocalDate date);
}
