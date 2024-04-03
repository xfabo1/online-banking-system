package cz.muni.fi.obs.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.AccountDto;
import cz.muni.fi.obs.api.UserCreateDto;
import cz.muni.fi.obs.api.UserDto;
import cz.muni.fi.obs.api.UserUpdateDto;
import cz.muni.fi.obs.enums.Nationality;
import cz.muni.fi.obs.facade.UserManagementFacade;
import cz.muni.fi.obs.util.JsonConvertor;

@WebMvcTest(controllers = UserController.class)
public class UserControllerWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserManagementFacade userManagementFacade;

	@Test
	public void createUser_userCreated_returnsUser() throws Exception {
		UserCreateDto userCreateDto = new UserCreateDto("Joe", "Doe", "123456789", "test@gmail.com",
				new Date(), Nationality.CZ.name(), "900101/1234");
		UserDto userDto = new UserDto("1","Joe", "Doe", "123456789", "test@gmail.com",
				new Date(), Nationality.CZ, "900101/123", false);
		when(userManagementFacade.createUser(userCreateDto)).thenReturn(userDto);

		String responseJson = mockMvc.perform(post("/v1/users/create")
						.content(JsonConvertor.convertObjectToJson(userCreateDto))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString(StandardCharsets.UTF_8);
		UserDto userDtoResponse = JsonConvertor.convertJsonToObject(responseJson, UserDto.class);

		verify(userManagementFacade).createUser(userCreateDto);
		assertThat(userDtoResponse).isEqualTo(userDto);
	}

	@Test
	public void getUser_userFound_returnsUser() throws Exception {
		UserDto userDto = new UserDto("1","Joe", "Doe", "123456789", "test@gmail.com",
				new Date(), Nationality.CZ, "900101/123", false);
		when(userManagementFacade.getUser("1")).thenReturn(userDto);

		String responseJson = mockMvc.perform(get("/v1/users/{userId}", "1")
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString(StandardCharsets.UTF_8);
		UserDto userDtoResponse = JsonConvertor.convertJsonToObject(responseJson, UserDto.class);
		verify(userManagementFacade).getUser("1");
		assertThat(userDtoResponse).isEqualTo(userDto);
	}

	@Test
	public void getUser_userNotFound_returns404() throws Exception {
		when(userManagementFacade.getUser("1")).thenReturn(null);

		mockMvc.perform(get("/v1/users/{userId}", "1")
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound());

		verify(userManagementFacade).getUser("1");
	}

	@Test
	public void updateUser_userUpdated_returnsUser() throws Exception {
		UserUpdateDto userUpdateDto = new UserUpdateDto("Joe", "Doe", "123456789", "test@gmail.com");
		UserDto userDto = new UserDto("1","Joe", "Doe", "123456789", "test@gmail.com",
				new Date(), Nationality.CZ, "900101/123", false);
		when(userManagementFacade.updateUser("1", userUpdateDto)).thenReturn(userDto);

		String responseJson = mockMvc.perform(put("/v1/users/{userId}", "1")
						.content(JsonConvertor.convertObjectToJson(userUpdateDto))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString(StandardCharsets.UTF_8);
		UserDto userDtoResponse = JsonConvertor.convertJsonToObject(responseJson, UserDto.class);

		verify(userManagementFacade).updateUser("1", userUpdateDto);
		assertThat(userDtoResponse).isEqualTo(userDto);
	}

	@Test
	public void deactivateUser_userDeactivated_returnsUser() throws Exception {
		UserDto userDto = new UserDto("1","Joe", "Doe", "123456789", "test@gmail.com",
				new Date(), Nationality.CZ, "900101/123", false);
		when(userManagementFacade.deactivateUser("1")).thenReturn(userDto);

		String responseJson = mockMvc.perform(post("/v1/users/{userId}/deactivate", "1")
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString(StandardCharsets.UTF_8);
		UserDto userDtoResponse = JsonConvertor.convertJsonToObject(responseJson, UserDto.class);


		verify(userManagementFacade).deactivateUser("1");
		assertThat(userDtoResponse).isEqualTo(userDto);
	}

	@Test
	public void activateUser_userActivated_returnsUser() throws Exception {
		UserDto userDto = new UserDto("1","Joe", "Doe", "123456789", "test@gmail.com",
				new Date(), Nationality.CZ, "900101/123", true);
		when(userManagementFacade.activateUser("1")).thenReturn(userDto);

		String responseJson = mockMvc.perform(post("/v1/users/{userId}/activate", "1")
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString(StandardCharsets.UTF_8);
		UserDto userDtoResponse = JsonConvertor.convertJsonToObject(responseJson, UserDto.class);

		verify(userManagementFacade).activateUser("1");
		assertThat(userDtoResponse).isEqualTo(userDto);
	}

	@Test
	public void createUserAccount_accountCreated_returnsAccount() throws Exception {
		AccountCreateDto accountCreateDto = new AccountCreateDto("1234567890", "Joe's Account");
		AccountDto accountDto = new AccountDto("1", "1234567890", "Joe's Account");
		when(userManagementFacade.createAccount("1", accountCreateDto)).thenReturn(accountDto);

		String responseJson = mockMvc.perform(post("/v1/users/{userId}/accounts/create", "1")
						.content(JsonConvertor.convertObjectToJson(accountCreateDto))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString(StandardCharsets.UTF_8);
		AccountDto accountDtoResponse = JsonConvertor.convertJsonToObject(responseJson, AccountDto.class);

		verify(userManagementFacade).createAccount("1", accountCreateDto);
		assertThat(accountDtoResponse).isEqualTo(accountDto);
	}

	@Test
	public void getUserAccounts_accountsFound_returnsAccounts() throws Exception {
		List<AccountDto> accounts = Arrays.asList(
				new AccountDto("1", "1234567890", "Joe's Account"),
				new AccountDto("2", "0987654321", "Jane's Account")
		);
		when(userManagementFacade.getUserAccounts("1")).thenReturn(accounts);

		String responseJson = mockMvc.perform(get("/v1/users/{userId}/accounts/", "1")
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString(StandardCharsets.UTF_8);
		List<AccountDto> accountDtoResponse = JsonConvertor.convertJsonToObject(responseJson,
				new TypeReference<>() {});

		verify(userManagementFacade).getUserAccounts("1");
		assertThat(accountDtoResponse).hasSize(2).containsExactly(accounts.get(0), accounts.get(1));
	}
}
