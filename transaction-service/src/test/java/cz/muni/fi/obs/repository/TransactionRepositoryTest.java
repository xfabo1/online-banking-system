package cz.muni.fi.obs.repository;

import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@Sql(value = { "/initialize_db.sql" }, executionPhase = BEFORE_TEST_CLASS)
@Sql(value = { "/drop_all.sql" }, executionPhase = AFTER_TEST_CLASS)
@DataJpaTest
@ActiveProfiles("test")
public class TransactionRepositoryTest {

	@Autowired
	private TransactionRepository transactionRepository;

	@Test
	public void findTransactionsByWithdrawsFromId_transactionsFound_returnsTransactions() {
        List<TransactionDbo> transactions = transactionRepository.findTransactionsDbosByWithdrawsFromId("1");

		assertThat(transactions)
				.hasSize(2)
				.extracting(TransactionDbo::getNote, TransactionDbo::getVariableSymbol)
				.containsExactlyInAnyOrder(
						tuple("note-1", "00"),
						tuple("note-7", "66"));

		insertTransaction();

		List<TransactionDbo> transactionsAfterSaving = transactionRepository
                .findTransactionsDbosByWithdrawsFromId("1");

		assertThat(transactionsAfterSaving)
				.hasSize(3)
				.extracting(TransactionDbo::getNote, TransactionDbo::getVariableSymbol)
				.containsExactlyInAnyOrder(
						tuple("note-1", "00"),
						tuple("note-7", "66"),
						tuple("note-10", "1010"));

		removeTransaction();
	}

	@Test
	public void findTransactionsByWithdrawsFromId_transactionsNotFound_returnsEmptyList() {
        List<TransactionDbo> transactions = transactionRepository.findTransactionsDbosByWithdrawsFromId("non-existing");

		assertThat(transactions).hasSize(0);
	}

	@Test
	public void findTransactionsByDepositsToId_transactionsFound_returnsTransactions() {
        List<TransactionDbo> transactions = transactionRepository.findTransactionsDboByDepositsToId("2");

		assertThat(transactions)
				.hasSize(2)
				.extracting(TransactionDbo::getNote, TransactionDbo::getVariableSymbol)
				.containsExactlyInAnyOrder(
						tuple("note-1", "00"),
						tuple("note-5", "44"));

		insertTransaction();

		List<TransactionDbo> transactionsAfterSaving = transactionRepository
                .findTransactionsDboByDepositsToId("2");

		assertThat(transactionsAfterSaving)
				.hasSize(3)
				.extracting(TransactionDbo::getNote, TransactionDbo::getVariableSymbol)
				.containsExactlyInAnyOrder(
						tuple("note-1", "00"),
						tuple("note-5", "44"),
						tuple("note-10", "1010"));

		removeTransaction();
	}

	@Test
	public void findTransactionsByDepositsToId_transactionsNotFound_returnsEmptyList() {
		List<TransactionDbo> transactions = transactionRepository
                .findTransactionsDboByDepositsToId("non-existing");

		assertThat(transactions).hasSize(0);
	}

	@Test
	public void findTransactionHistory_transactionsFound_returnsTransactions() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<TransactionDbo> transactions = transactionRepository.findTransactionHistory("1", pageable);

		assertThat(transactions).isNotEmpty();
		assertThat(transactions.getContent())
				.hasSize(4)
				.extracting(TransactionDbo::getNote, TransactionDbo::getVariableSymbol)
				.containsExactlyInAnyOrder(
						tuple("note-1", "00"),
						tuple("note-2", "11"),
						tuple("note-7", "66"),
						tuple("note-9", "88"));
	}

	@Test
	public void findTransactionHistory_transactionsNotFound_returnsEmptyPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<TransactionDbo> transactions = transactionRepository
				.findTransactionHistory("non-existing", pageable);

		assertThat(transactions).isEmpty();
	}

	private void insertTransaction() {
		TransactionDbo transactionDbo = TransactionDbo.builder()
				.id("10")
				.withdrawsFrom(AccountDbo.builder()
						.id("1")
						.accountNumber("account-1")
						.currencyCode("CZK")
						.build())
				.depositsTo(AccountDbo.builder()
						.id("2")
						.accountNumber("account-2")
						.currencyCode("EUR")
						.build())
				.withdrawAmount(BigDecimal.ONE)
				.depositAmount(BigDecimal.ONE)
				.conversionRate(1.0)
				.note("note-10")
				.variableSymbol("1010")
				.build();

		transactionRepository.save(transactionDbo);
	}

	private void removeTransaction() {
		transactionRepository.deleteById("7");
	}
}
