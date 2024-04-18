package cz.muni.fi.obs.controller;

import static cz.muni.fi.obs.controller.TransactionController.TRANSACTION_PATH;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.controller.pagination.PagedResponse;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.facade.TransactionManagementFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping(TRANSACTION_PATH)
public class TransactionController {

	public static final String TRANSACTION_PATH = "/v1/transactions";
	private final TransactionManagementFacade facade;

	@Autowired
	public TransactionController(TransactionManagementFacade facade) {
		this.facade = facade;
	}

	@Operation(
			summary = "Get transaction by ID",
			description = "Finds a transaction by its ID",
			responses = {
					@ApiResponse(responseCode = "200", description = "Transaction found"),
					@ApiResponse(responseCode = "404", description = "Transaction not found")
			}
	)
	@GetMapping(value = "/transaction/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionDbo> getTransactionById(@PathVariable("id") String id) {
		log.info("Getting transaction by id: {}", id);
		Optional<TransactionDbo> transaction = facade.getTransactionById(id);
		return transaction.map(ResponseEntity::ok)
				.orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
	}

	@Operation(
			summary = "View transaction history",
			description = "Views transaction history for account",
			responses = {
					@ApiResponse(responseCode = "200", description = "Transaction history found"),
					@ApiResponse(responseCode = "404", description = "Transaction history not found")
			}
	)
	@GetMapping(value = "/account/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedResponse<TransactionDbo>> viewTransactionHistory(
			@PathVariable("accountNumber") String accountNumber,
			@RequestParam("pageNumber") int pageNumber,
			@RequestParam("pageSize") int pageSize) {
		log.info("Getting transaction history for account: {}", accountNumber);
		Page<TransactionDbo> page = facade.viewTransactionHistory(accountNumber, pageNumber, pageSize);
		if (page.isEmpty()) {
			log.info("Transaction history not found for account: {}", accountNumber);
			throw new ResourceNotFoundException("Transaction history not found");
		}
		return ResponseEntity.ok(PagedResponse.fromPage(page));
	}

	@Operation(
			summary = "Check account balance",
			description = "Checks account balance for account",
			responses = {
					@ApiResponse(responseCode = "200", description = "Account balance found"),
					@ApiResponse(responseCode = "404", description = "Account balance not found")
			}
	)
	@GetMapping(value = "/account/{accountNumber}/balance", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BigDecimal> checkAccountBalance(@PathVariable("accountNumber") String accountNumber) {
		log.info("Checking account balance for account: {}", accountNumber);
		return ResponseEntity.ok(facade.checkAccountBalance(accountNumber));
	}

	@Operation(
			summary = "Create a new transaction",
			description = "Creates a new transaction from request body",
			responses = {
					@ApiResponse(responseCode = "201", description = "Transaction created successfully"),
					@ApiResponse(responseCode = "400", description = "Invalid request body")
			}
	)
	@PostMapping(value = "/transaction/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createTransaction(@Valid @RequestBody TransactionCreateDto transaction) {
		log.info("Creating transaction: {}", transaction);
		facade.createTransaction(transaction);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}

