package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.UserCreateDto;
import cz.muni.fi.obs.api.UserUpdateDto;
import cz.muni.fi.obs.domain.Account;
import cz.muni.fi.obs.domain.User;
import cz.muni.fi.obs.facade.UserManagementFacade;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Validated
@RequestMapping("/users")
public class UserController {

    private final UserManagementFacade userManagementFacade;

    @Autowired
    public UserController(UserManagementFacade userManagementFacade) {
        this.userManagementFacade = userManagementFacade;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(
            @Valid @RequestBody UserCreateDto userAccountCreateDto
    ) {
        log.info("Creating user with birth number: " + userAccountCreateDto.getBirthNumber());
        User user = userManagementFacade.createUser(userAccountCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(
            @PathVariable("userId") String userId
    ) {
        log.info("Getting user with id: " + userId);
        User user = userManagementFacade.getUser(userId);
        if (user == null) {
            log.info("User with id: " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }
    @GetMapping("")
    public ResponseEntity<User[]> getAllUsers() {
        log.info("Getting all users");
        User[] users = userManagementFacade.getAllUsers();

        return ResponseEntity.ok(users);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable ("userId") String userId,
            @Valid @RequestBody UserUpdateDto userUpdateDto
    ) {
        log.info("Updating user with id: " + userId);
        User user = userManagementFacade.updateUser(userId, userUpdateDto);
        if (user == null) {
            log.info("User with id: " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/accounts/create")
    public ResponseEntity<Account> createUserAccount(
            @PathVariable("userId") String userId,
            @Valid @RequestBody AccountCreateDto accountCreateDto

    ) {
        log.info("Creating user account for user with id: " + userId);
        Account account = userManagementFacade.createAccount(userId, accountCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }
    @GetMapping("/{userId}/accounts")
    public ResponseEntity<Account[]> getUserAccounts(
            @PathVariable("userId") String userId
    ) {
        log.info("Getting user accounts for user with id: " + userId);
        Account[] accounts = userManagementFacade.getUserAccounts(userId);

        return ResponseEntity.ok(accounts);
    }

}
