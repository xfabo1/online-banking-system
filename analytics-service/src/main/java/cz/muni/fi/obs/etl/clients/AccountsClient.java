package cz.muni.fi.obs.etl.clients;

import cz.muni.fi.obs.etl.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(
        name = "accounts-client",
        url = "${etl.transaction-service.url}"
)
public interface AccountsClient {

    @PostMapping(value = "/v1/accounts/list")
    Page<AccountDto> listAccounts(@RequestBody Pageable pageable);
}
