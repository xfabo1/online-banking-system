package cz.muni.fi.obs.etl.clients;

import cz.muni.fi.obs.etl.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;


@FeignClient(
        name = "transaction-client",
        url = "${etl.transaction-service.url}"
)
public interface TransactionClient {
    @PostMapping(value = "/v1/transactions/{accountId}/list")
    ResponseEntity<Page<TransactionDto>> listTransactions(
            @PathVariable("accountId") String accountId,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("date") LocalDate date);
}
