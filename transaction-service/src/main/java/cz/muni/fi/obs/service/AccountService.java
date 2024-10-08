package cz.muni.fi.obs.service;

import java.util.List;
import java.util.UUID;

import cz.muni.fi.obs.http.CurrencyServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.repository.AccountRepository;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;

@Service
public class AccountService {

	private final AccountRepository repository;
	private final CurrencyServiceClient client;

	@Autowired
	public AccountService(AccountRepository repository, CurrencyServiceClient client) {
		this.repository = repository;
        this.client = client;
    }

	public AccountDbo createAccount(AccountCreateDto accountCreateDto) {

		var exists = client.currencyExists(accountCreateDto.currencyCode());
		if (!exists) {
			throw new ResourceNotFoundException("Currency with code " + accountCreateDto.currencyCode() + " does not exist");
		}
		var accountDbo = AccountDbo.builder()
				.id(UUID.randomUUID().toString())
				.currencyCode(accountCreateDto.currencyCode())
				.customerId(accountCreateDto.customerId())
				.build();
		return repository.save(accountDbo);
	}

	public AccountDbo findAccountByAccountId(String accountId) {
		return repository.findById(accountId)
				.orElseThrow(() -> new ResourceNotFoundException(AccountDbo.class, accountId));
	}

	public List<AccountDbo> findAccountsByCustomerId(String customerId) {
		return repository.findAccountDbosByCustomerId(customerId);
	}

    public Page<AccountDbo> listAccounts(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
