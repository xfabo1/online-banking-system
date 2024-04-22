package cz.muni.fi.obs.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.repository.AccountRepository;

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
				.accountNumber(accountCreateDto.accountNumber())
				.currencyCode(accountCreateDto.currencyCode())
				.customerId(accountCreateDto.customerId())
				.build();
		return repository.save(accountDbo);
	}

	public Optional<AccountDbo> findAccountByAccountNumber(String accountNumber) {
		return repository.findAccountDboByAccountNumber(accountNumber);
	}
}
