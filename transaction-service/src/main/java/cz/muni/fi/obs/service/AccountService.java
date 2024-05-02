package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

	private final AccountRepository repository;

	@Autowired
	public AccountService(AccountRepository repository) {
		this.repository = repository;
	}

	public AccountDbo createAccount(AccountCreateDto accountCreateDto) {
		var accountDbo = AccountDbo.builder()
				.id(UUID.randomUUID().toString())
				.currencyCode(accountCreateDto.currencyCode())
				.customerId(accountCreateDto.customerId())
				.build();
		return repository.save(accountDbo);
	}

	public AccountDbo findAccountByAccountId(String accountNumber) {
		return repository.findById(accountNumber)
				.orElseThrow(() -> new ResourceNotFoundException(AccountDbo.class, accountNumber));
	}

	public List<AccountDbo> findAccountsByCustomerId(String customerId) {
		return repository.findAccountDbosByCustomerId(customerId);
	}

    public Page<AccountDbo> listAccounts(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
