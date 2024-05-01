package cz.muni.fi.obs.facade;


import cz.muni.fi.obs.api.*;
import cz.muni.fi.obs.data.dbo.User;
import cz.muni.fi.obs.service.UserAccountService;
import cz.muni.fi.obs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

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


    public UserDto updateUser(UUID userId, UserUpdateDto userUpdateDto) {
        User user = userService.updateUser(userId, userUpdateDto);
        return UserDto.fromUser(user);
    }

    public UserDto deactivateUser(UUID userId) {
        User user = userService.deactivateUser(userId);
        return UserDto.fromUser(user);
    }

    public UserDto activateUser(UUID userId) {
        User user = userService.activateUser(userId);
        return UserDto.fromUser(user);
    }

    public UserDto getUser(UUID userId) {
        User user = userService.getUser(userId);
        return UserDto.fromUser(user);
    }

    public Page<UserDto> findUsers(UserSearchParamsPaginatedDto searchParams) {
        Page<User> users = userService.findUsers(searchParams);
        return users.map(UserDto::fromUser);
    }

    public AccountDto createAccount(UUID userId, AccountCreateDto accountCreateDto) {
        return userAccountService.create(userId, accountCreateDto);
    }

    public List<AccountDto> getUserAccounts(UUID userId) {
        userService.getUser(userId);
        return userAccountService.getUserAccounts(userId);
    }
}
