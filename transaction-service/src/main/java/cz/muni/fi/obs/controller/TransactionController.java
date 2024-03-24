package cz.muni.fi.obs.controller;

import static cz.muni.fi.obs.controller.TransactionController.TRANSACTION_PATH;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.facade.TransactionManagementFacade;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(TRANSACTION_PATH)
public class TransactionController {

	public static final String TRANSACTION_PATH = "/transactions";
	private final TransactionManagementFacade facade;

	@Autowired
	public TransactionController(TransactionManagementFacade facade) {
		this.facade = facade;
	}

	@GetMapping("/transaction/{id}")
	public ResponseEntity<TransactionDbo> getTransactionById(@PathVariable("id") String id) {
		log.info("Getting transaction by id: {}", id);
		return ResponseEntity.ok(facade.getTransactionById(id));
	}

	@GetMapping("/account/{accountId}")
	public ResponseEntity<List<TransactionDbo>> viewTransactionHistory(@PathVariable("accountId") String accountId) {
		log.info("Getting transaction history for account: {}", accountId);
		return ResponseEntity.ok(facade.viewTransactionHistory(accountId));
	}

	@GetMapping("/account/{accountId}/balance")
	public ResponseEntity<Long> checkAccountBalance(@PathVariable("accountId") String accountId) {
		log.info("Checking account balance for account: {}", accountId);
		return ResponseEntity.ok(facade.checkAccountBalance(accountId));
	}

	@PostMapping("/transaction/create")
	public ResponseEntity<Void> createTransaction(@RequestBody TransactionCreateDto transaction) {
		log.info("Creating transaction: {}", transaction);
		facade.createTransaction(transaction);
		return ResponseEntity.ok().build();
	}
}

