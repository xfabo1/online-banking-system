package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.api.*;
import cz.muni.fi.obs.domain.Account;
import cz.muni.fi.obs.domain.User;
import cz.muni.fi.obs.enums.Nationality;
import cz.muni.fi.obs.facade.UserManagementFacade;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public PagedResponse<User> getUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Date birthDate,
            @RequestParam(required = false) String birthNumber,
            @RequestParam(required = false, defaultValue = "true") boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        UserSearchParamsDto searchParams = UserSearchParamsDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .email(email)
                .birthDate(birthDate)
                .birthNumber(birthNumber)
                .active(active)
                .page(page)
                .pageSize(pageSize)
                .build();

        // Retrieve users based on search parameters
        return userManagementFacade.findUsers(searchParams);
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

    @PostMapping("/{userId}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable ("userId") String userId) {
        log.info("Deactivating user with id: " + userId);
        User user = userManagementFacade.deactivateUser(userId);
        if (user == null) {
            log.info("User with id: " + userId + " not found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/activate")
    public ResponseEntity<User> activateUser(@PathVariable ("userId") String userId) {
        log.info("Activating user with id: " + userId);
        User user = userManagementFacade.activateUser(userId);
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
