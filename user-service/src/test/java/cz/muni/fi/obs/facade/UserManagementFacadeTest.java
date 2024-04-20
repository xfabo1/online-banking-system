package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.api.*;
import cz.muni.fi.obs.data.dbo.User;
import cz.muni.fi.obs.data.enums.Nationality;
import cz.muni.fi.obs.service.UserAccountService;
import cz.muni.fi.obs.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        UserCreateDto userCreateDto = new UserCreateDto("Joe",
                                                        "Doe",
                                                        "123456789",
                                                        "test@gmail.com",
                                                        LocalDate.now(),
                                                        Nationality.CZ,
                                                        "900101/1234"
        );
        User user = new User("Joe",
                             "Doe",
                             "123456789",
                             "test@gmail.com",
                             userCreateDto.birthDate(),
                             Nationality.CZ,
                             "900101/123",
                             true
        );
        UserDto userDto = new UserDto(user.getId(),
                                      "Joe",
                                      "Doe",
                                      "123456789",
                                      "test@gmail.com",
                                      userCreateDto.birthDate(),
                                      Nationality.CZ,
                                      "900101/123",
                                      true
        );
        when(userService.createUser(userCreateDto)).thenReturn(user);

        UserDto response = userManagementFacade.createUser(userCreateDto);

        verify(userService).createUser(userCreateDto);
        assertThat(response).isEqualTo(userDto);
    }

    @Test
    public void getUser_userFound_returnsUser() {
        User user = new User("Joe",
                             "Doe",
                             "123456789",
                             "test@gmail.com",
                             LocalDate.now(),
                             Nationality.CZ,
                             "900101/123",
                             true
        );
        UserDto userDto = new UserDto(user.getId(),
                                      "Joe",
                                      "Doe",
                                      "123456789",
                                      "test@gmail.com",
                                      user.getBirthDate(),
                                      Nationality.CZ,
                                      "900101/123",
                                      true
        );
        when(userService.getUser(user.getId())).thenReturn(user);

        UserDto response = userManagementFacade.getUser(user.getId());

        verify(userService).getUser(user.getId());
        assertThat(response).isEqualTo(userDto);
    }

    @Test
    public void updateUser_userUpdated_returnsUser() {
        UserUpdateDto userUpdateDto = new UserUpdateDto(Optional.of("Joe"),
                                                        Optional.of("Doe"),
                                                        Optional.of("123456789"),
                                                        Optional.of("test@gmail.com")
        );
        User user = new User("Joe",
                             "Doe",
                             "123456789",
                             "test@gmail.com",
                             LocalDate.now(),
                             Nationality.CZ,
                             "900101/123",
                             true
        );
        UserDto userDto = new UserDto(user.getId(),
                                      "Joe",
                                      "Doe",
                                      "123456789",
                                      "test@gmail.com",
                                      user.getBirthDate(),
                                      Nationality.CZ,
                                      "900101/123",
                                      true
        );
        when(userService.updateUser(user.getId(), userUpdateDto)).thenReturn(user);

        UserDto response = userManagementFacade.updateUser(user.getId(), userUpdateDto);

        verify(userService).updateUser(user.getId(), userUpdateDto);
        assertThat(response).isEqualTo(userDto);
    }

    @Test
    public void deactivateUser_userDeactivated_returnsUser() {
        User user = new User("Joe",
                             "Doe",
                             "123456789",
                             "test@gmail.com",
                             LocalDate.now(),
                             Nationality.CZ,
                             "900101/123",
                             true
        );
        UserDto userDto = new UserDto(user.getId(),
                                      "Joe",
                                      "Doe",
                                      "123456789",
                                      "test@gmail.com",
                                      user.getBirthDate(),
                                      Nationality.CZ,
                                      "900101/123",
                                      true
        );
        when(userService.deactivateUser(user.getId())).thenReturn(user);

        UserDto response = userManagementFacade.deactivateUser(user.getId());

        verify(userService).deactivateUser(user.getId());
        assertThat(response).isEqualTo(userDto);
    }

    @Test
    public void activateUser_userActivated_returnsUser() {
        User user = new User("Joe",
                             "Doe",
                             "123456789",
                             "test@gmail.com",
                             LocalDate.now(),
                             Nationality.CZ,
                             "900101/123",
                             true
        );
        UserDto userDto = new UserDto(user.getId(),
                                      "Joe",
                                      "Doe",
                                      "123456789",
                                      "test@gmail.com",
                                      user.getBirthDate(),
                                      Nationality.CZ,
                                      "900101/123",
                                      true
        );
        when(userService.activateUser(user.getId())).thenReturn(user);

        UserDto response = userManagementFacade.activateUser(user.getId());

        verify(userService).activateUser(user.getId());
        assertThat(response).isEqualTo(userDto);
    }

    @Test
    public void createAccount_accountCreated_returnsAccount() {
        AccountCreateDto accountCreateDto = new AccountCreateDto("1234567890", "Joe's Account");
        AccountDto account = new AccountDto(UUID.randomUUID(), "1234567890", "Joe's Account");
        when(userAccountService.create(account.id(), accountCreateDto)).thenReturn(account);

        AccountDto response = userManagementFacade.createAccount(account.id(), accountCreateDto);

        verify(userAccountService).create(account.id(), accountCreateDto);
        assertThat(response).isEqualTo(account);
    }

    @Test
    public void getUserAccounts_accountsFound_returnsAccounts() {
        UUID userId = UUID.randomUUID();
        List<AccountDto> accounts = Arrays.asList(new AccountDto(UUID.randomUUID(), "1234567890", "Joe's Account"),
                                                  new AccountDto(UUID.randomUUID(), "0987654321", "Jane's Account")
        );
        when(userAccountService.getUserAccounts(userId)).thenReturn(accounts);

        List<AccountDto> response = userManagementFacade.getUserAccounts(userId);

        verify(userAccountService).getUserAccounts(userId);
        assertThat(response).hasSize(2).containsExactly(accounts.get(0), accounts.get(1));
    }
}
