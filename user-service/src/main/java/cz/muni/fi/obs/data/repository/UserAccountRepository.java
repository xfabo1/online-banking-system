package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.AccountDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserAccountRepository {
    // TODO: replace with transaction-microservice api calls

    public AccountDto create(AccountCreateDto account) {
        return new AccountDto(
                UUID.randomUUID(),
                account.accountNumber(),
                account.currencyCode()
        );
    }

    public List<AccountDto> findByUserId(UUID userId) {
        return new ArrayList<>();
    }

}
