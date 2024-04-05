package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.facade.TransactionManagementFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static cz.muni.fi.obs.controller.TransactionController.TRANSACTION_PATH;

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
	@GetMapping("/transaction/{id}")
	public ResponseEntity<TransactionDbo> getTransactionById(@PathVariable("id") String id) {
		log.info("Getting transaction by id: {}", id);
		TransactionDbo transaction = facade.getTransactionById(id);
		if (transaction == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(transaction);
	}

	@Operation(
			summary = "View transaction history",
			description = "Views transaction history for account",
			responses = {
					@ApiResponse(responseCode = "200", description = "Transaction history found"),
					@ApiResponse(responseCode = "404", description = "Transaction history not found")
			}
	)
	@GetMapping("/account/{accountId}")
	public ResponseEntity<Page<TransactionDbo>> viewTransactionHistory(
			@PathVariable("accountId") String accountId,
			@RequestParam("pageNumber") int pageNumber,
			@RequestParam(value = "pageSize") int pageSize) {
		log.info("Getting transaction history for account: {}", accountId);
		Page<TransactionDbo> page = facade.viewTransactionHistory(accountId, pageNumber, pageSize);
		if (page.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(page);
	}

	@Operation(
			summary = "Check account balance",
			description = "Checks account balance for account",
			responses = {
					@ApiResponse(responseCode = "200", description = "Account balance found"),
					@ApiResponse(responseCode = "404", description = "Account balance not found")
			}
	)
	@GetMapping("/account/{accountId}/balance")
	public ResponseEntity<BigDecimal> checkAccountBalance(@PathVariable("accountId") String accountId) {
		log.info("Checking account balance for account: {}", accountId);
		BigDecimal balance = facade.checkAccountBalance(accountId);
		if (balance == null) {
			log.info("Account balance not found for account: {}", accountId);
			throw new ResourceNotFoundException("Account balance not found");
		}
		return ResponseEntity.ok(balance);
	}

	@Operation(
			summary = "Create a new transaction",
			description = "Creates a new transaction from request body",
			responses = {
					@ApiResponse(responseCode = "201", description = "Transaction created successfully"),
					@ApiResponse(responseCode = "400", description = "Invalid request body")
			}
	)
	@PostMapping("/transaction/create")
	public ResponseEntity<Void> createTransaction(@Valid @RequestBody TransactionCreateDto transaction) {
		log.info("Creating transaction: {}", transaction);
		facade.createTransaction(transaction);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}

