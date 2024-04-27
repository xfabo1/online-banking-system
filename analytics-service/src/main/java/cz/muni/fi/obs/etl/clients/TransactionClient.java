package cz.muni.fi.obs.etl.clients;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * TODO: add operation that lists transactions for specific account_id, check if transaction service already contains endpoint for this
 * if it does just copy it, else you have to create operation GET Page<TransactionDto> (account_id) in transaction_service
 */
@FeignClient(
        name = "transaction-client",
        url = "${etl.transaction-service.url}"
)
public interface TransactionClient {
}
