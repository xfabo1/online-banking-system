package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.facade.TransactionManagementFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cz.muni.fi.obs.controller.AccountController.ACCOUNT_PATH;

@Slf4j
@Validated
@RestController
@RequestMapping(ACCOUNT_PATH)
public class AccountController {

	public static final String ACCOUNT_PATH = "/v1/accounts";

	private final TransactionManagementFacade facade;

	@Autowired
	public AccountController(TransactionManagementFacade facade) {
		this.facade = facade;
	}

	@Operation(
			summary = "Create an account for customer",
			description = "Creates an account for customer from request body",
			responses = {
					@ApiResponse(responseCode = "201", description = "Account created successfully"),
					@ApiResponse(responseCode = "400", description = "Invalid request body")
			}
	)
	@PostMapping("/create")
	public ResponseEntity<Void> createAccount(@Valid @RequestBody AccountCreateDto account) {
		facade.createAccount(account);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(
			summary = "Find account by ID",
			description = "Finds an account by its ID",
			responses = {
					@ApiResponse(responseCode = "200", description = "Account found"),
					@ApiResponse(responseCode = "404", description = "Account not found")
			}

	)
	@GetMapping("/account/{id}")
	public ResponseEntity<AccountDbo> findAccountById(@PathVariable("id") String id) {
		AccountDbo account = facade.findAccountById(id);
		if (account == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(account);
	}
}
