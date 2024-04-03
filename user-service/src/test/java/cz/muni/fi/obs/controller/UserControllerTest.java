package cz.muni.fi.obs.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import org.springframework.http.ResponseEntity;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.AccountDto;
import cz.muni.fi.obs.api.UserCreateDto;
import cz.muni.fi.obs.api.UserDto;
import cz.muni.fi.obs.api.UserUpdateDto;
import cz.muni.fi.obs.enums.Nationality;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.facade.UserManagementFacade;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@Mock
	private UserManagementFacade userManagementFacade;

	@InjectMocks
	private UserController userController;

	@Test
	public void createUser_userCreated_returnsUser() {
		UserCreateDto userCreateDto = new UserCreateDto("Joe", "Doe", "123456789", "test@gmail.com",
				LocalDate.now(), Nationality.CZ.name(), "900101/1234");
		UserDto userDto = new UserDto("1", "Joe", "Doe", "123456789", "test@gmail.com",
				LocalDate.now(), Nationality.CZ, "900101/123", false);
		when(userManagementFacade.createUser(userCreateDto)).thenReturn(userDto);

		ResponseEntity<UserDto> response = userController.createUser(userCreateDto);

		verify(userManagementFacade).createUser(userCreateDto);
		assertThat(response.getBody()).isEqualTo(userDto);
	}

	@Test
	public void getUser_userFound_returnsUser() {
		UserDto userDto = new UserDto("1", "Joe", "Doe", "123456789", "test@gmail.com",
				LocalDate.now(), Nationality.CZ, "900101/123", false);
		when(userManagementFacade.getUser("1")).thenReturn(userDto);

		ResponseEntity<UserDto> response = userController.getUser("1");

		verify(userManagementFacade).getUser("1");
		assertThat(response.getBody()).isEqualTo(userDto);
	}

	@Test
	public void getUser_userNotFound_returns404() {
		when(userManagementFacade.getUser("1")).thenReturn(null);

		assertThatThrownBy(() -> userController.getUser("1")).isInstanceOf(ResourceNotFoundException.class)
				.hasMessage("User with id: 1 not found");
		verify(userManagementFacade).getUser("1");
	}

	@Test
	public void updateUser_userUpdated_returnsUser() {
		UserUpdateDto userUpdateDto = new UserUpdateDto("Joe", "Doe", "123456789", "test@gmail.com");
		UserDto userDto = new UserDto("1", "Joe", "Doe", "123456789", "test@gmail.com",
				LocalDate.now(), Nationality.CZ, "900101/123", false);
		when(userManagementFacade.updateUser("1", userUpdateDto)).thenReturn(userDto);

		ResponseEntity<UserDto> response = userController.updateUser("1", userUpdateDto);

		verify(userManagementFacade).updateUser("1", userUpdateDto);
		assertThat(response.getBody()).isEqualTo(userDto);
	}

	@Test
	public void deactivateUser_userDeactivated_returnsUser() {
		UserDto userDto = new UserDto("1", "Joe", "Doe", "123456789", "test@gmail.com",
				LocalDate.now(), Nationality.CZ, "900101/123", false);
		when(userManagementFacade.deactivateUser("1")).thenReturn(userDto);

		ResponseEntity<UserDto> response = userController.deactivateUser("1");

		verify(userManagementFacade).deactivateUser("1");
		assertThat(response.getBody()).isEqualTo(userDto);
	}

	@Test
	public void activateUser_userActivated_returnsUser() {
		UserDto userDto = new UserDto("1", "Joe", "Doe", "123456789", "test@gmail.com",
				LocalDate.now(), Nationality.CZ, "900101/123", true);
		when(userManagementFacade.activateUser("1")).thenReturn(userDto);

		ResponseEntity<UserDto> response = userController.activateUser("1");

		verify(userManagementFacade).activateUser("1");
		assertThat(response.getBody()).isEqualTo(userDto);
	}

	@Test
	public void createUserAccount_accountCreated_returnsAccount() {
		AccountCreateDto accountCreateDto = new AccountCreateDto("1234567890", "Joe's Account");
		AccountDto accountDto = new AccountDto("1", "1234567890", "Joe's Account");
		when(userManagementFacade.createAccount("1", accountCreateDto)).thenReturn(accountDto);

		ResponseEntity<AccountDto> response = userController.createUserAccount("1", accountCreateDto);

		verify(userManagementFacade).createAccount("1", accountCreateDto);
		assertThat(response.getBody()).isEqualTo(accountDto);
	}

	@Test
	public void getUserAccounts_accountsFound_returnsAccounts() {
		List<AccountDto> accounts = Arrays.asList(
				new AccountDto("1", "1234567890", "Joe's Account"),
				new AccountDto("2", "0987654321", "Jane's Account")
		);
		when(userManagementFacade.getUserAccounts("1")).thenReturn(accounts);

		ResponseEntity<List<AccountDto>> response = userController.getUserAccounts("1");

		verify(userManagementFacade).getUserAccounts("1");
		assertThat(response.getBody()).hasSize(2).containsExactly(accounts.get(0), accounts.get(1));
	}
}
