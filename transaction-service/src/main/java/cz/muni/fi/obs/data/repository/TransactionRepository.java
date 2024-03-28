package cz.muni.fi.obs.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import cz.muni.fi.obs.data.dbo.TransactionDbo;

@Repository
public class TransactionRepository {

	public TransactionDbo getTransactionById(String id) {
		return TransactionDbo.builder()
				.id("1")
				.depositsTo("1")
				.conversionRate(1)
				.note("note")
				.withdrawAmount(1)
				.withdrawsFrom("1")
				.depositAmount(1)
				.variableSymbol("123")
				.state("state")
				.build();
	}

	public List<TransactionDbo> getTransactionsByDepositId(String depositId) {
		return List.of(
				TransactionDbo.builder()
						.id("1")
						.depositsTo("1")
						.conversionRate(1)
						.note("note")
						.withdrawAmount(1)
						.withdrawsFrom("1")
						.depositAmount(1)
						.variableSymbol("123")
						.state("state")
						.build(),
				TransactionDbo.builder()
						.id("2")
						.depositsTo("1")
						.conversionRate(2)
						.note("note")
						.withdrawAmount(2)
						.withdrawsFrom("2")
						.depositAmount(2)
						.variableSymbol("123")
						.state("state")
						.build()
		);
	}

	public List<TransactionDbo> getTransactionsByWithdrawId(String withdrawId) {
		return List.of(
				TransactionDbo.builder()
						.id("1")
						.depositsTo("1")
						.conversionRate(1)
						.note("note")
						.withdrawAmount(1)
						.withdrawsFrom("1")
						.depositAmount(1)
						.variableSymbol("123")
						.state("state")
						.build(),
				TransactionDbo.builder()
						.id("2")
						.depositsTo("1")
						.conversionRate(2)
						.note("note")
						.withdrawAmount(2)
						.withdrawsFrom("1")
						.depositAmount(2)
						.variableSymbol("123")
						.state("state")
						.build()
		);
	}

	public Page<TransactionDbo> getTransactionHistory(String accountNumber, int pageNumber, int pageSize) {
		var transactions = List.of(
				TransactionDbo.builder()
						.id("1")
						.conversionRate(1)
						.note("note")
						.withdrawAmount(1)
						.withdrawsFrom("1")
						.depositAmount(1)
						.variableSymbol("123")
						.state("state")
						.build(),
				TransactionDbo.builder()
						.id("2")
						.depositsTo("2")
						.conversionRate(2)
						.note("note")
						.withdrawAmount(2)
						.withdrawsFrom("2")
						.depositAmount(2)
						.variableSymbol("123")
						.state("state")
						.build()
		);

		return new PageImpl<>(transactions, PageRequest.of(pageNumber, pageSize), transactions.size());
	}

	public void createTransaction(TransactionDbo transaction) {
		// create transaction
	}
}