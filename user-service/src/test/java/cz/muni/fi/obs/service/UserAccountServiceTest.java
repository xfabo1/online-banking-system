package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.domain.Account;
import cz.muni.fi.obs.data.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

	@Mock
	private UserAccountRepository userAccountRepository;

	@InjectMocks
	private UserAccountService userAccountService;

	@Test
	void create_accountCreated_returnsAccount() {
		AccountCreateDto accountCreateDto = new AccountCreateDto("1234567890", "Joe's Account");
		Account account = new Account("1", "1234567890", "Joe's Account");
		when(userAccountRepository.create(any())).thenReturn(account);

		Account response = userAccountService.create("1", accountCreateDto);

		verify(userAccountRepository).create(any());
		assertThat(response).isEqualTo(account);
	}

	@Test
	void getUserAccounts_accountsFound_returnsAccounts() {
		Account account1 = new Account("1", "1234567890", "Joe's Account");
		Account account2 = new Account("1", "0987654321", "Joe's Other Account");
		List<Account> accounts = Arrays.asList(account1, account2);
		when(userAccountRepository.findByUserId("1")).thenReturn(accounts);

		List<Account> response = userAccountService.getUserAccounts("1");

		verify(userAccountRepository).findByUserId("1");
		assertThat(response).isEqualTo(accounts);
	}
}
