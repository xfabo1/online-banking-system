package cz.muni.fi.obs.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;

import cz.muni.fi.obs.TestData;
import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.controller.pagination.PagedResponse;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.facade.TransactionManagementFacade;
import util.JsonConvertor;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	TransactionManagementFacade transactionManagementFacade;

	@Test
	void getTransactionById_TransactionFound_ReturnsTransaction() throws Exception {
		String transactionId = TestData.withdrawTransactions.getFirst().getId();
		when(transactionManagementFacade.getTransactionById(transactionId))
				.thenReturn(Optional.of(TestData.withdrawTransactions.getFirst()));

		var response = mockMvc.perform(get("/v1/transactions/transaction/{id}", transactionId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		TransactionDbo actualTransaction = JsonConvertor.convertJsonToObject(response, TransactionDbo.class);
		assertThat(actualTransaction).isEqualTo(TestData.withdrawTransactions.getFirst());
	}

	@Test
	void viewTransactionHistory_returnsHistory() throws Exception {
		var allTransaction = List.of(
				TestData.withdrawTransactions.getFirst(),
				TestData.withdrawTransactions.get(1),
				TestData.depositTransactions.getFirst(),
				TestData.depositTransactions.get(1)
		);
		when(transactionManagementFacade.viewTransactionHistory("test", 0, 10))
				.thenReturn(new PageImpl<>(allTransaction, PageRequest.of(0, 10),
						allTransaction.size()));

		var response = mockMvc.perform(get("/v1/transactions/account/{accountNumber}", "test")
						.queryParam("pageNumber", "0")
						.queryParam("pageSize", "10")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		PagedResponse<TransactionDbo> actualTransactions = JsonConvertor.convertJsonToObject(response,
				new TypeReference<>() {});
		assertThat(actualTransactions.records()).hasSize(4);

	}

	@Test
	void checkAccountBalance_returnsBalance() throws Exception {
		when(transactionManagementFacade.checkAccountBalance(TestData.accountId)).thenReturn(BigDecimal.valueOf(42));

		var response = mockMvc.perform(get("/v1/transactions/account/{accountNumber}/balance", TestData.accountId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		BigDecimal actualBalance = JsonConvertor.convertJsonToObject(response, BigDecimal.class);
		assertThat(actualBalance).isEqualTo(BigDecimal.valueOf(42));
	}

	@Test
	public void createTransaction_createsTransaction() throws Exception {
		TransactionCreateDto transactionCreateDto = new TransactionCreateDto(
				TestData.withdrawTransactions.getFirst().getWithdrawsFrom().getAccountNumber(),
				TestData.withdrawTransactions.getFirst().getDepositsTo().getAccountNumber(),
				TestData.withdrawTransactions.getFirst().getWithdrawAmount(),
				TestData.withdrawTransactions.getFirst().getDepositAmount(),
				TestData.withdrawTransactions.getFirst().getNote(),
				TestData.withdrawTransactions.getFirst().getVariableSymbol()
		);

		mockMvc.perform(post("/v1/transactions/transaction/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonConvertor.convertObjectToJson(transactionCreateDto)))
				.andExpect(status().isCreated());

	}
}
