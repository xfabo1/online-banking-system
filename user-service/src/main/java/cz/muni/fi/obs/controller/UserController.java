package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.api.*;
import cz.muni.fi.obs.enums.Nationality;
import cz.muni.fi.obs.facade.UserManagementFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@OpenAPIDefinition(
        info = @Info(title = "Bank User Management API",
                version = "1.1",
                description = """
                        Microservice for managing bank user account and their bank accounts. The API has operations for:
                        - creating a new user
                        - getting user by ID
                        - getting users with optional search parameters
                        - updating user by ID
                        - deactivating user by ID
                        - activating user by ID
                        - creating user account for user by ID
                        - getting user accounts by user ID
                        """,
                contact = @Contact(name = "Vilem Gottwald", email = "553627@mail.muni.cz"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        servers = @Server(description = "local server", url = "{scheme}://{server}:{port}", variables = {
                @ServerVariable(name = "scheme", defaultValue = "http"),
                @ServerVariable(name = "server", defaultValue = "localhost"),
                @ServerVariable(name = "port", defaultValue = "8080"),
        })
)
@Tag(name = "User management", description = "Microservice for managing users and their bank accounts")
// metadata for inclusion into OpenAPI document
@RequestMapping("/v1/users")
public class UserController {

    private final UserManagementFacade userManagementFacade;

    @Autowired
    public UserController(UserManagementFacade userManagementFacade) {
        this.userManagementFacade = userManagementFacade;
    }

    @Operation(summary = "Get available nationalities")
    @GetMapping("/nationalities")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Nationality[]> getNationalities() {
        return ResponseEntity.ok(Nationality.values());
    }

    @Operation(summary = "Create a new user")
    @PostMapping("/create")
    @CrossOrigin(origins = "*")
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserCreateDto userCreateDto
    ) {
        log.info("Creating user with birth number: " + userCreateDto.birthNumber());
        UserDto user = userManagementFacade.createUser(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{userId}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<UserDto> getUser(
            @Parameter(description = "ID of the user to be retrieved") @PathVariable("userId") String userId
    ) {
        log.info("Getting user with id: " + userId);
        UserDto user = userManagementFacade.getUser(userId);
        if (user == null) {
            log.info("User with id: " + userId + " not found");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get users with optional search parameters")
    @GetMapping("")
    @CrossOrigin(origins = "*")
    public Page<UserDto> getUsers(
            @ParameterObject UserSearchParamsDto searchParams
    ) {
        // Retrieve users based on search parameters
        return userManagementFacade.findUsers(searchParams);
    }

    @Operation(summary = "Update user by ID")
    @PutMapping("/{userId}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "ID of the user to be updated") @PathVariable("userId") String userId,
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

    @Operation(summary = "Deactivate user by ID")
    @PostMapping("/{userId}/deactivate")
    @CrossOrigin(origins = "*")
    public ResponseEntity<UserDto> deactivateUser(@PathVariable("userId") String userId) {
        log.info("Deactivating user with id: " + userId);
        UserDto user = userManagementFacade.deactivateUser(userId);
        if (user == null) {
            log.info("User with id: " + userId + " not found");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Activate user by ID")
    @PostMapping("/{userId}/activate")
    @CrossOrigin(origins = "*")
    public ResponseEntity<UserDto> activateUser(@PathVariable("userId") String userId) {
        log.info("Activating user with id: " + userId);
        UserDto user = userManagementFacade.activateUser(userId);
        if (user == null) {
            log.info("User with id: " + userId + " not found");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Create user account for user by ID")
    @PostMapping("/{userId}/accounts/create")
    @CrossOrigin(origins = "*")
    public ResponseEntity<AccountDto> createUserAccount(
            @Parameter(description = "ID of the user for whom the account will be created") @PathVariable("userId") String userId,
            @Valid @RequestBody AccountCreateDto accountCreateDto
    ) {
        log.info("Creating user account for user with id: " + userId);
        AccountDto account = userManagementFacade.createAccount(userId, accountCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @Operation(summary = "Get user accounts by user ID")
    @GetMapping("/{userId}/accounts")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<AccountDto>> getUserAccounts(
            @Parameter(description = "ID of the user whose accounts are to be retrieved") @PathVariable("userId") String userId
    ) {
        log.info("Getting user accounts for user with id: " + userId);
        List<AccountDto> accounts = userManagementFacade.getUserAccounts(userId);
        return ResponseEntity.ok(accounts);
    }
}
