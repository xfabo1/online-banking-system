package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.data.UserAccountRepository;
import cz.muni.fi.obs.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public Account create(String userId, AccountCreateDto accountCreateDto) {
        Account account = new Account(
                userId,
                accountCreateDto.getAccountNumber(),
                accountCreateDto.getCurrencyCode()
        );

        return userAccountRepository.create(account);
    }

    public Account[] getUserAccounts(String userId) {
        return userAccountRepository.findByUserId(userId);
    }
}
