package cz.muni.fi.obs.etl.clients;

import cz.muni.fi.obs.etl.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(
        name = "accounts-client",
        url = "${etl.transaction-service.url}"
)
public interface AccountsClient {

    @GetMapping(value = "/v1/accounts/list", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<AccountDto> listAccounts(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize);
}
