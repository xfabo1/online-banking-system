package cz.muni.fi.obs.etl.clients;

import cz.muni.fi.obs.etl.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * TODO: add operation that lists transactions for specific account_id, check if transaction service already contains endpoint for this
 * if it does just copy it, else you have to create operation GET Page<TransactionDto> (account_id) in transaction_service
 */
@FeignClient(
        name = "transaction-client",
        url = "${etl.transaction-service.url}"
)
public interface TransactionClient {
    // TODO accountNumber seems good enough but not sure
    @GetMapping(value = "/v1/transactions/account/{accountNumber}")
    Page<TransactionDto> listTransactions(@PathVariable("accountNumber") String accountNumber);
}
