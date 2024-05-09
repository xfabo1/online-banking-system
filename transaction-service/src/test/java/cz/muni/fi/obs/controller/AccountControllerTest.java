package cz.muni.fi.obs.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.facade.TransactionManagementFacade;
import cz.muni.fi.obs.util.JsonConvertor;

@WebMvcTest
@ContextConfiguration(classes = {AccountController.class, ControllerAdvice.class})
class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransactionManagementFacade facade;

	@Test
	void createAccount_validRequest_returnsNothing() throws Exception {
		AccountCreateDto accountCreateDto = new AccountCreateDto("owner", "CZK");

		mockMvc.perform(post("/v1/accounts/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonConvertor.convertObjectToJson(accountCreateDto)))
				.andExpect(status().isCreated());

		verify(facade).createAccount(accountCreateDto);
	}

	@Test
	void createAccount_invalidRequest_returnsBadRequest() throws Exception {
		AccountCreateDto accountCreateDto = new AccountCreateDto(null, "CZK");

		mockMvc.perform(post("/v1/accounts/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonConvertor.convertObjectToJson(accountCreateDto)))
				.andExpect(status().isBadRequest());

		verifyNoInteractions(facade);
	}

	@Test
	void findAccountById_accountFound_returnsAccount() throws Exception {
		AccountDbo expectedAccount = AccountDbo.builder()
				.id("1")
				.customerId("owner")
				.currencyCode("CZK")
				.build();

		when(facade.findAccountByAccountId("1")).thenReturn(expectedAccount);

		var response = mockMvc.perform(get("/v1/accounts/account/{accountNumber}", "1")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		var actualAccount = JsonConvertor.convertJsonToObject(response, AccountDbo.class);

		verify(facade).findAccountByAccountId("1");
		assertThat(actualAccount).isEqualTo(expectedAccount);
	}

	@Test
	void testFindAccountById_nonExistingId_returnsNotFound() throws Exception {
		when(facade.findAccountByAccountId(any()))
				.thenThrow(new ResourceNotFoundException(AccountDbo.class, "0"));

		mockMvc.perform(get("/v1/accounts/account/{accountNumber}", "0")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		verify(facade).findAccountByAccountId("0");
	}
}
