package cz.muni.fi.obs.controller;

import static cz.muni.fi.obs.controller.AccountController.ACCOUNT_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.facade.TransactionManagementFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

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
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AccountDbo> createAccount(@Valid @RequestBody AccountCreateDto account) {
		log.info("Creating account: {}", account);
		AccountDbo accountDbo = facade.createAccount(account);
		return ResponseEntity.status(HttpStatus.CREATED).body(accountDbo);
	}

	@Operation(
			summary = "Find account by account number",
			description = "Finds an account by its account number",
			responses = {
					@ApiResponse(responseCode = "200", description = "Account found"),
					@ApiResponse(responseCode = "404", description = "Account not found")
			}

	)
	@GetMapping(value = "/account/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AccountDbo> findAccountById(@PathVariable("accountNumber") String accountNumber) {
		return facade.findAccountByAccountNumber(accountNumber)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> {
							log.info("Account not found: {}", accountNumber);
							return new ResourceNotFoundException(accountNumber);
						}
				);
	}
}
