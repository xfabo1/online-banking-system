package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.api.*;
import cz.muni.fi.obs.data.enums.Nationality;
import cz.muni.fi.obs.exceptions.UserNotFoundException;
import cz.muni.fi.obs.facade.UserManagementFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserManagementFacade userManagementFacade;

    @InjectMocks
    private UserController userController;

    @Test
    public void createUser_userCreated_returnsUser() {
        UserCreateDto userCreateDto = new UserCreateDto("Joe",
                                                        "Doe",
                                                        "123456789",
                                                        "test@gmail.com",
                                                        LocalDate.now(),
                                                        Nationality.CZ,
                                                        "900101/1234"
        );
        UserDto userDto = new UserDto(UUID.randomUUID(),
                                      "Joe",
                                      "Doe",
                                      "123456789",
                                      "test@gmail.com",
                                      LocalDate.now(),
                                      Nationality.CZ,
                                      "900101/123",
                                      false
        );
        when(userManagementFacade.createUser(userCreateDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.createUser(userCreateDto);

        verify(userManagementFacade).createUser(userCreateDto);
        assertThat(response.getBody()).isEqualTo(userDto);
    }

    @Test
    public void getUser_userFound_returnsUser() {
        UserDto userDto = new UserDto(UUID.randomUUID(),
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

        ResponseEntity<UserDto> response = userController.getUser(userDto.id());

        verify(userManagementFacade).getUser(userDto.id());
        assertThat(response.getBody()).isEqualTo(userDto);
    }

    @Test
    public void getUser_userNotFound_returns404() {
        UUID nonexistentUserId = UUID.randomUUID();
        when(userManagementFacade.getUser(nonexistentUserId)).thenThrow(UserNotFoundException.class);

        assertThatThrownBy(() -> userController.getUser(nonexistentUserId))
                .isInstanceOf(UserNotFoundException.class);
        verify(userManagementFacade).getUser(nonexistentUserId);
    }

    @Test
    public void updateUser_userUpdated_returnsUser() {
        UserUpdateDto userUpdateDto = new UserUpdateDto(Optional.of("Joe"),
                                                        Optional.of("Doe"),
                                                        Optional.of("123456789"),
                                                        Optional.of("test@gmail.com")
        );
        UserDto userDto = new UserDto(UUID.randomUUID(),
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

        ResponseEntity<UserDto> response = userController.updateUser(userDto.id(), userUpdateDto);

        verify(userManagementFacade).updateUser(userDto.id(), userUpdateDto);
        assertThat(response.getBody()).isEqualTo(userDto);
    }

    @Test
    public void deactivateUser_userDeactivated_returnsUser() {
        UserDto userDto = new UserDto(UUID.randomUUID(),
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

        ResponseEntity<UserDto> response = userController.deactivateUser(userDto.id());

        verify(userManagementFacade).deactivateUser(userDto.id());
        assertThat(response.getBody()).isEqualTo(userDto);
    }

    @Test
    public void activateUser_userActivated_returnsUser() {
        UserDto userDto = new UserDto(UUID.randomUUID(),
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

        ResponseEntity<UserDto> response = userController.activateUser(userDto.id());

        verify(userManagementFacade).activateUser(userDto.id());
        assertThat(response.getBody()).isEqualTo(userDto);
    }

    @Test
    public void createUserAccount_accountCreated_returnsAccount() {
        UUID userId = UUID.randomUUID();
        AccountCreateDto accountCreateDto = new AccountCreateDto("1234567890", "Joe's Account");
        AccountDto accountDto = new AccountDto(UUID.randomUUID(), "1234567890", "Joe's Account");
        when(userManagementFacade.createAccount(userId, accountCreateDto)).thenReturn(accountDto);

        ResponseEntity<AccountDto> response = userController.createUserAccount(userId, accountCreateDto);

        verify(userManagementFacade).createAccount(userId, accountCreateDto);
        assertThat(response.getBody()).isEqualTo(accountDto);
    }

    @Test
    public void getUserAccounts_accountsFound_returnsAccounts() {
        UUID userId = UUID.randomUUID();
        List<AccountDto> accounts = Arrays.asList(new AccountDto(UUID.randomUUID(), "1234567890", "Joe's Account"),
                                                  new AccountDto(UUID.randomUUID(), "0987654321", "Jane's Account")
        );
        when(userManagementFacade.getUserAccounts(userId)).thenReturn(accounts);

        ResponseEntity<List<AccountDto>> response = userController.getUserAccounts(userId);

        verify(userManagementFacade).getUserAccounts(userId);
        assertThat(response.getBody()).hasSize(2).containsExactly(accounts.get(0), accounts.get(1));
    }
}
