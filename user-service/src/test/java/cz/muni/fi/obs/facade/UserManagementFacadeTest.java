package cz.muni.fi.obs.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.AccountDto;
import cz.muni.fi.obs.api.UserCreateDto;
import cz.muni.fi.obs.api.UserDto;
import cz.muni.fi.obs.api.UserUpdateDto;
import cz.muni.fi.obs.domain.Account;
import cz.muni.fi.obs.domain.User;
import cz.muni.fi.obs.enums.Nationality;
import cz.muni.fi.obs.service.UserAccountService;
import cz.muni.fi.obs.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserManagementFacadeTest {

	@Mock
	private UserService userService;
	@Mock
	private UserAccountService userAccountService;

	@InjectMocks
	private UserManagementFacade userManagementFacade;

	@Test
	public void createUser_userCreated_returnsUser() {
		UserCreateDto userCreateDto = new UserCreateDto("Joe", "Doe", "123456789", "test@gmail.com",
				LocalDate.now(), Nationality.CZ.name(), "900101/1234");
		User user = new User("Joe", "Doe", "123456789", "test@gmail.com",userCreateDto.birthDate(), Nationality.CZ, "900101/123", true);
		UserDto userDto = new UserDto(user.getId(), "Joe", "Doe", "123456789", "test@gmail.com",
				userCreateDto.birthDate(), Nationality.CZ, "900101/123", true);
		when(userService.createUser(userCreateDto)).thenReturn(user);

		UserDto response = userManagementFacade.createUser(userCreateDto);

		verify(userService).createUser(userCreateDto);
		assertThat(response).isEqualTo(userDto);
	}

	@Test
	public void getUser_userFound_returnsUser() {
		User user = new User("Joe", "Doe", "123456789", "test@gmail.com",
				LocalDate.now(), Nationality.CZ, "900101/123", true);
		UserDto userDto = new UserDto(user.getId(), "Joe", "Doe", "123456789", "test@gmail.com",
				user.getBirthDate(), Nationality.CZ, "900101/123", true);
		when(userService.getUser("1")).thenReturn(user);

		UserDto response = userManagementFacade.getUser("1");

		verify(userService).getUser("1");
		assertThat(response).isEqualTo(userDto);
	}

	@Test
	public void updateUser_userUpdated_returnsUser() {
		UserUpdateDto userUpdateDto = new UserUpdateDto("Joe", "Doe", "123456789", "test@gmail.com");
		User user = new User("Joe", "Doe", "123456789", "test@gmail.com",
				LocalDate.now(), Nationality.CZ, "900101/123", true);
		UserDto userDto = new UserDto(user.getId(), "Joe", "Doe", "123456789", "test@gmail.com",
				user.getBirthDate(), Nationality.CZ, "900101/123", true);
		when(userService.updateUser("1", userUpdateDto)).thenReturn(user);

		UserDto response = userManagementFacade.updateUser("1", userUpdateDto);

		verify(userService).updateUser("1", userUpdateDto);
		assertThat(response).isEqualTo(userDto);
	}

	@Test
	public void deactivateUser_userDeactivated_returnsUser() {
		User user = new User("Joe", "Doe", "123456789", "test@gmail.com",
				LocalDate.now(), Nationality.CZ, "900101/123", true);
		UserDto userDto = new UserDto(user.getId(), "Joe", "Doe", "123456789", "test@gmail.com",
				user.getBirthDate(), Nationality.CZ, "900101/123", true);
		when(userService.deactivateUser("1")).thenReturn(user);

		UserDto response = userManagementFacade.deactivateUser("1");

		verify(userService).deactivateUser("1");
		assertThat(response).isEqualTo(userDto);
	}

	@Test
	public void activateUser_userActivated_returnsUser() {
		User user = new User("Joe", "Doe", "123456789", "test@gmail.com",
				LocalDate.now(), Nationality.CZ, "900101/123", true);
		UserDto userDto = new UserDto(user.getId(), "Joe", "Doe", "123456789", "test@gmail.com",
				user.getBirthDate(), Nationality.CZ, "900101/123", true);
		when(userService.activateUser("1")).thenReturn(user);

		UserDto response = userManagementFacade.activateUser("1");

		verify(userService).activateUser("1");
		assertThat(response).isEqualTo(userDto);
	}

	@Test
	public void createAccount_accountCreated_returnsAccount() {
		AccountCreateDto accountCreateDto = new AccountCreateDto("1234567890", "Joe's Account");
		Account account = new Account("1", "1234567890", "Joe's Account");
		AccountDto accountDto = new AccountDto(account.getId(), "1234567890", "Joe's Account");
		when(userAccountService.create("1", accountCreateDto)).thenReturn(account);

		AccountDto response = userManagementFacade.createAccount("1", accountCreateDto);

		verify(userAccountService).create("1", accountCreateDto);
		assertThat(response).isEqualTo(accountDto);
	}

	@Test
	public void getUserAccounts_accountsFound_returnsAccounts() {
		List<Account> accounts = Arrays.asList(
				new Account("1", "1234567890", "Joe's Account"),
				new Account("2", "0987654321", "Jane's Account")
		);
		List<AccountDto> accountsDto = Arrays.asList(
				new AccountDto(accounts.get(0).getId(), "1234567890", "Joe's Account"),
				new AccountDto(accounts.get(1).getId(), "0987654321", "Jane's Account")
		);
		when(userAccountService.getUserAccounts("1")).thenReturn(accounts);

		List<AccountDto> response = userManagementFacade.getUserAccounts("1");

		verify(userAccountService).getUserAccounts("1");
		assertThat(response).hasSize(2).containsExactly(accountsDto.get(0), accountsDto.get(1));
	}
}
