package cz.muni.fi.obs.data.repository;

import org.springframework.stereotype.Repository;

import cz.muni.fi.obs.data.dbo.AccountDbo;

@Repository
public class AccountRepository {

	public void createAccount(AccountDbo accountDbo) {
		// TODO: implement
	}

	public AccountDbo findAccountById(String id) {
		return AccountDbo.builder()
				.id("1")
				.customerId("owner")
				.currencyCode("CZK")
				.accountNumber("1234567890")
				.build();
	}
}
