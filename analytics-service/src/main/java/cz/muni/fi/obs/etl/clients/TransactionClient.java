package cz.muni.fi.obs.etl.clients;

import cz.muni.fi.obs.etl.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;


@FeignClient(
        name = "transaction-client",
        url = "${etl.transaction-service.url}"
)
public interface TransactionClient {
    @GetMapping(value = "/v1/transactions/{accountId}/list",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<TransactionDto>> listTransactions(
            @PathVariable("accountId") String accountId,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("date") LocalDate date);
}
