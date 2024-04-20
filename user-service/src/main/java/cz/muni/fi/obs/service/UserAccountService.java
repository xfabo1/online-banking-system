package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.AccountDto;
import cz.muni.fi.obs.data.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;

    }

    public AccountDto create(UUID userId, AccountCreateDto accountCreateDto) {
        return userAccountRepository.create(accountCreateDto);
    }

    public List<AccountDto> getUserAccounts(UUID userId) {
        return userAccountRepository.findByUserId(userId);
    }
}
