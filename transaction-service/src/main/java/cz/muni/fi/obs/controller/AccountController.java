package cz.muni.fi.obs.controller;

import static cz.muni.fi.obs.controller.AccountController.ACCOUNT_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.facade.TransactionManagementFacade;

@RestController
@RequestMapping(ACCOUNT_PATH)
public class AccountController {

	public static final String ACCOUNT_PATH = "/v1/accounts";

	private final TransactionManagementFacade facade;

	@Autowired
	public AccountController(TransactionManagementFacade facade) {
		this.facade = facade;
	}

	@PostMapping("/create")
	public ResponseEntity<Void> createAccount(@RequestBody AccountCreateDto account) {
		facade.createAccount(account);
		return ResponseEntity.ok().build();
	}
}
