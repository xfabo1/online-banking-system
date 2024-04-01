package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.api.*;
import cz.muni.fi.obs.enums.Nationality;
import cz.muni.fi.obs.facade.UserManagementFacade;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/user-management/v1/users")
public class UserController {

    private final UserManagementFacade userManagementFacade;

    @Autowired
    public UserController(UserManagementFacade userManagementFacade) {
        this.userManagementFacade = userManagementFacade;
    }

    @GetMapping("/nationalities")
    public ResponseEntity<Nationality[]> getNationalities() {
        return ResponseEntity.ok(Nationality.values());

    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserCreateDto userCreateDto
    ) {
        log.info("Creating user with birth number: " + userCreateDto.birthNumber());
        UserDto user = userManagementFacade.createUser(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable("userId") String userId
    ) {
        log.info("Getting user with id: " + userId);
        UserDto user = userManagementFacade.getUser(userId);
        if (user == null) {
            log.info("User with id: " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("")
    public Page<UserDto> getUsers(
            @ParameterObject UserSearchParamsDto searchParams
    ) {

        // Retrieve users based on search parameters
        return userManagementFacade.findUsers(searchParams);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("userId") String userId,
            @Valid @RequestBody UserUpdateDto userUpdateDto
    ) {
        log.info("Updating user with id: " + userId);
        UserDto user = userManagementFacade.updateUser(userId, userUpdateDto);
        if (user == null) {
            log.info("User with id: " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/deactivate")
    public ResponseEntity<UserDto> deactivateUser(@PathVariable("userId") String userId) {
        log.info("Deactivating user with id: " + userId);
        UserDto user = userManagementFacade.deactivateUser(userId);
        if (user == null) {
            log.info("User with id: " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/activate")
    public ResponseEntity<UserDto> activateUser(@PathVariable("userId") String userId) {
        log.info("Activating user with id: " + userId);
        UserDto user = userManagementFacade.activateUser(userId);
        if (user == null) {
            log.info("User with id: " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }


    @PostMapping("/{userId}/accounts/create")
    public ResponseEntity<AccountDto> createUserAccount(
            @PathVariable("userId") String userId,
            @Valid @RequestBody AccountCreateDto accountCreateDto

    ) {
        log.info("Creating user account for user with id: " + userId);
        AccountDto account = userManagementFacade.createAccount(userId, accountCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<AccountDto>> getUserAccounts(
            @PathVariable("userId") String userId
    ) {
        log.info("Getting user accounts for user with id: " + userId);
        List<AccountDto> accounts = userManagementFacade.getUserAccounts(userId);

        return ResponseEntity.ok(accounts);
    }

}
