package cz.muni.fi.obs.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.muni.fi.obs.api.*;
import cz.muni.fi.obs.data.enums.Nationality;
import cz.muni.fi.obs.exceptions.UserNotFoundException;
import cz.muni.fi.obs.facade.UserManagementFacade;
import cz.muni.fi.obs.util.JsonConvertor;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
public class UserControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserManagementFacade userManagementFacade;

    @Test
    public void createUser_userCreated_returnsUser() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto("Joe",
                                                        "Doe",
                                                        "123456789",
                                                        "test@gmail.com",
                                                        LocalDate.of(2001, 4, 13),
                                                        Nationality.SK,
                                                        "010413/2215"
        );
        UserDto userDto = new UserDto(UUID.randomUUID(),
                                      "553628@muni.cz",
                                      userCreateDto.firstName(),
                                      userCreateDto.lastName(),
                                      userCreateDto.phoneNumber(),
                                      userCreateDto.email(),
                                      userCreateDto.birthDate(),
                                      userCreateDto.nationality(),
                                      userCreateDto.birthNumber(),
                                      true
        );
        when(userManagementFacade.createUser(userCreateDto)).thenReturn(userDto);

        String responseJson = mockMvc.perform(post("/v1/users/create").content(JsonConvertor.convertObjectToJson(
                                             userCreateDto)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE))
                                     .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(
                        StandardCharsets.UTF_8);
        UserDto userDtoResponse = JsonConvertor.convertJsonToObject(responseJson, UserDto.class);

        verify(userManagementFacade).createUser(userCreateDto);
        assertThat(userDtoResponse).isEqualTo(userDto);
    }

    @Test
    public void getUser_userFound_returnsUser() throws Exception {
        UserDto userDto = new UserDto(UUID.randomUUID(),
                                      "123456@muni.cz",
                                      "Joe",
                                      "Doe",
                                      "123456789",
                                      "test@gmail.com",
                                      LocalDate.now(),
                                      Nationality.CZ,
                                      "900101/123",
                                      false
        );
        when(userManagementFacade.getUser(userDto.id())).thenReturn(userDto);

        String responseJson =
                mockMvc.perform(get("/v1/users/{userId}", userDto.id()).accept(MediaType.APPLICATION_JSON_VALUE))
                       .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(
                               StandardCharsets.UTF_8);
        UserDto userDtoResponse = JsonConvertor.convertJsonToObject(responseJson, UserDto.class);
        verify(userManagementFacade).getUser(userDto.id());
        assertThat(userDtoResponse).isEqualTo(userDto);
    }

    @Test
    public void getUser_userNotFound_returns404() throws Exception {
        UUID nonexistentUserId = UUID.randomUUID();
        when(userManagementFacade.getUser(nonexistentUserId)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(get("/v1/users/{userId}", nonexistentUserId).accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());

        verify(userManagementFacade).getUser(nonexistentUserId);
    }

    @Test
    public void updateUser_userUpdated_returnsUser() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto(Optional.of("Joe"),
                                                        Optional.of("Doe"),
                                                        Optional.of("123456789"),
                                                        Optional.of("test@gmail.com")
        );

        JSONObject userUpdateJson = new JSONObject();
        userUpdateJson.put("firstName", "Joe");
        userUpdateJson.put("lastName", "Doe");
        userUpdateJson.put("phoneNumber", "123456789");
        userUpdateJson.put("email", "test@gmail.com");

        UserDto userDto = new UserDto(UUID.randomUUID(),
                                      "553628@muni.cz",
                                      "Joe",
                                      "Doe",
                                      "123456789",
                                      "test@gmail.com",
                                      LocalDate.now(),
                                      Nationality.CZ,
                                      "900101/123",
                                      false
        );
        when(userManagementFacade.updateUser(userDto.id(), userUpdateDto)).thenReturn(userDto);

        String responseJson = mockMvc.perform(put("/v1/users/{userId}", userDto.id())
                                                      .content(userUpdateJson.toString())
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .accept(MediaType.APPLICATION_JSON_VALUE))
                                     .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(
                        StandardCharsets.UTF_8);
        UserDto userDtoResponse = JsonConvertor.convertJsonToObject(responseJson, UserDto.class);

        verify(userManagementFacade).updateUser(userDto.id(), userUpdateDto);
        assertThat(userDtoResponse).isEqualTo(userDto);
    }

    @Test
    public void deactivateUser_userDeactivated_returnsUser() throws Exception {
        UserDto userDto = new UserDto(UUID.randomUUID(),
                                      "553628@muni.cz",
                                      "Joe",
                                      "Doe",
                                      "123456789",
                                      "test@gmail.com",
                                      LocalDate.now(),
                                      Nationality.CZ,
                                      "900101/123",
                                      false
        );
        when(userManagementFacade.deactivateUser(userDto.id())).thenReturn(userDto);

        String responseJson = mockMvc.perform(post("/v1/users/{userId}/deactivate",
                                                   userDto.id()
                                     ).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn().getResponse()
                                     .getContentAsString(StandardCharsets.UTF_8);
        UserDto userDtoResponse = JsonConvertor.convertJsonToObject(responseJson, UserDto.class);


        verify(userManagementFacade).deactivateUser(userDto.id());
        assertThat(userDtoResponse).isEqualTo(userDto);
    }

    @Test
    public void activateUser_userActivated_returnsUser() throws Exception {
        UserDto userDto = new UserDto(UUID.randomUUID(),
                                      "553628@muni.cz",
                                      "Joe",
                                      "Doe",
                                      "123456789",
                                      "test@gmail.com",
                                      LocalDate.now(),
                                      Nationality.CZ,
                                      "900101/123",
                                      true
        );
        when(userManagementFacade.activateUser(userDto.id())).thenReturn(userDto);

        String responseJson = mockMvc.perform(post("/v1/users/{userId}/activate",
                                                   userDto.id()
                                     ).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn().getResponse()
                                     .getContentAsString(StandardCharsets.UTF_8);
        UserDto userDtoResponse = JsonConvertor.convertJsonToObject(responseJson, UserDto.class);

        verify(userManagementFacade).activateUser(userDto.id());
        assertThat(userDtoResponse).isEqualTo(userDto);
    }

    @Test
    public void createUserAccount_accountCreated_returnsAccount() throws Exception {
        AccountCreateDto accountCreateDto = new AccountCreateDto("1234567890", "Joe's Account");
        AccountDto accountDto = new AccountDto(UUID.randomUUID(), "1234567890", "Joe's Account");
        when(userManagementFacade.createAccount(accountDto.id(), accountCreateDto)).thenReturn(accountDto);

        String responseJson = mockMvc.perform(post("/v1/users/{userId}/accounts/create", accountDto.id())
                                                      .content(JsonConvertor.convertObjectToJson(accountCreateDto))
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .accept(MediaType.APPLICATION_JSON_VALUE))
                                     .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(
                        StandardCharsets.UTF_8);
        AccountDto accountDtoResponse = JsonConvertor.convertJsonToObject(responseJson, AccountDto.class);

        verify(userManagementFacade).createAccount(accountDto.id(), accountCreateDto);
        assertThat(accountDtoResponse).isEqualTo(accountDto);
    }

    @Test
    public void getUserAccounts_accountsFound_returnsAccounts() throws Exception {
        UUID userId = UUID.randomUUID();
        List<AccountDto> accounts = Arrays.asList(new AccountDto(UUID.randomUUID(), "1234567890", "Joe's Account"),
                                                  new AccountDto(UUID.randomUUID(), "0987654321", "Jane's Account")
        );
        when(userManagementFacade.getUserAccounts(userId)).thenReturn(accounts);

        String responseJson = mockMvc.perform(get("/v1/users/{userId}/accounts",
                                                  userId
                                     ).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn().getResponse()
                                     .getContentAsString(StandardCharsets.UTF_8);
        List<AccountDto> accountDtoResponse = JsonConvertor.convertJsonToObject(responseJson, new TypeReference<>() {
        });

        verify(userManagementFacade).getUserAccounts(userId);
        assertThat(accountDtoResponse).hasSize(2).containsExactly(accounts.get(0), accounts.get(1));
    }
}
