package cz.muni.fi.obs.facade;


import cz.muni.fi.obs.api.*;
import cz.muni.fi.obs.domain.Account;
import cz.muni.fi.obs.domain.User;
import cz.muni.fi.obs.service.UserAccountService;
import cz.muni.fi.obs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserManagementFacade {

    private final UserService userService;
    private final UserAccountService userAccountService;

    @Autowired
    public UserManagementFacade(UserService userService, UserAccountService userAccountService) {
        this.userService = userService;
        this.userAccountService = userAccountService;
    }

    public UserDto createUser(UserCreateDto userCreateDto) {
        User user = userService.createUser(userCreateDto);
        return UserDto.fromUser(user);
    }


    public UserDto updateUser(String userId, UserUpdateDto userUpdateDto) {
        User user = userService.updateUser(userId, userUpdateDto);
        return UserDto.fromUser(user);
    }

    public UserDto deactivateUser(String userId) {
        User user = userService.deactivateUser(userId);
        return UserDto.fromUser(user);
    }

    public UserDto activateUser(String userId) {
        User user = userService.activateUser(userId);
        return UserDto.fromUser(user);
    }

    public UserDto getUser(String userId) {
        User user = userService.getUser(userId);
        return UserDto.fromUser(user);
    }

    public Page<UserDto> findUsers(UserSearchParamsDto searchParams) {
        Page<User> users = userService.findUsers(searchParams);
        return users.map(UserDto::fromUser);
    }

    public AccountDto createAccount(String userId, AccountCreateDto accountCreateDto) {
        Account account = userAccountService.create(userId, accountCreateDto);
        return AccountDto.fromAccount(account);
    }

    public List<AccountDto> getUserAccounts(String userId) {
        List<Account> accounts = userAccountService.getUserAccounts(userId);

        return accounts.stream().map(AccountDto::fromAccount).toList();
    }
}
