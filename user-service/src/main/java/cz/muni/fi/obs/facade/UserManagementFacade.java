package cz.muni.fi.obs.facade;


import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.UserCreateDto;
import cz.muni.fi.obs.api.UserUpdateDto;
import cz.muni.fi.obs.domain.Account;
import cz.muni.fi.obs.domain.User;
import cz.muni.fi.obs.service.UserAccountService;
import cz.muni.fi.obs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserManagementFacade {

    private final UserService userService;
    private final UserAccountService userAccountService;

    @Autowired
    public UserManagementFacade(UserService userService, UserAccountService userAccountService) {
        this.userService = userService;
        this.userAccountService = userAccountService;
    }

    public User createUser(UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }


    public User updateUser(String userId, UserUpdateDto userUpdateDto) {
        return userService.updateUser(userId, userUpdateDto);
    }

    public User getUser(String userId) {
        return userService.getUser(userId);
    }

    public User[] getAllUsers() {
        return userService.getAllUsers();
    }

    public Account createAccount(String userId, AccountCreateDto accountCreateDto) {
        return userAccountService.create(userId, accountCreateDto);
    }

    public Account[] getUserAccounts(String userId) {
        return userAccountService.getUserAccounts(userId);
    }
}
