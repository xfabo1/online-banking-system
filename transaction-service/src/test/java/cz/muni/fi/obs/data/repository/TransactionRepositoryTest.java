package cz.muni.fi.obs.data.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.dbo.TransactionState;

@Sql(value = { "/initialize_db.sql" }, executionPhase = BEFORE_TEST_CLASS)
@Sql(value = { "/drop_all.sql" }, executionPhase = AFTER_TEST_CLASS)
@DataJpaTest
@ActiveProfiles("test")
public class TransactionRepositoryTest {

	@Autowired
	private TransactionRepository transactionRepository;

	@Test
	public void findTransactionsForAccountForSpecificDay_transactionFound_returnsTransactions() {
		LocalDate today = LocalDate.now();
		Instant startOfDay = today.atStartOfDay().toInstant(ZoneOffset.UTC);
		Instant endOfDay = today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

		Page<TransactionDbo> transactionDbos = transactionRepository.listTransactions("1", Pageable.ofSize(50), startOfDay, endOfDay);

		assertThat(transactionDbos.getTotalElements()).isEqualTo(4);
		assertThat(transactionDbos.getTotalPages()).isEqualTo(1);
		assertThat(transactionDbos.getContent().size()).isEqualTo(4);
		assertThat(transactionDbos.getContent().stream().allMatch(trans -> trans.getWithdrawsFrom().getId().equals("1") ||
				trans.getDepositsTo().getId().equals("1"))).isTrue();
	}

	@Test
	public void findTransactionsForSpecificDay_wrongDay_returnsEmptyPage() {
		LocalDate today = LocalDate.now();
		Instant startOfDay = today.atStartOfDay().plusDays(1).toInstant(ZoneOffset.UTC);
		Instant endOfDay = today.plusDays(2).atStartOfDay().toInstant(ZoneOffset.UTC);

		Page<TransactionDbo> transactionDbos = transactionRepository.listTransactions("1", Pageable.ofSize(50), startOfDay, endOfDay);

		assertThat(transactionDbos.getTotalElements()).isEqualTo(0);
		assertThat(transactionDbos.getTotalPages()).isEqualTo(0);
		assertThat(transactionDbos.getContent().size()).isEqualTo(0);
	}

	@Test
	public void findTransactionsByWithdrawsFromId_transactionsFound_returnsTransactions() {
        List<TransactionDbo> transactions = transactionRepository.findTransactionsDboByWithdrawsFrom_Id("1");

		assertThat(transactions)
				.hasSize(2)
				.extracting(TransactionDbo::getNote, TransactionDbo::getVariableSymbol)
				.containsExactlyInAnyOrder(
						tuple("note-1", "00"),
						tuple("note-7", "66"));

		insertTransaction();

		List<TransactionDbo> transactionsAfterSaving = transactionRepository
                .findTransactionsDboByWithdrawsFrom_Id("1");

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
        List<TransactionDbo> transactions = transactionRepository.findTransactionsDboByWithdrawsFrom_Id("non-existing");

		assertThat(transactions).hasSize(0);
	}

	@Test
	public void findTransactionsByDepositsToId_transactionsFound_returnsTransactions() {
        List<TransactionDbo> transactions = transactionRepository.findTransactionsDboByDepositsTo_Id("2");

		assertThat(transactions)
				.hasSize(2)
				.extracting(TransactionDbo::getNote, TransactionDbo::getVariableSymbol)
				.containsExactlyInAnyOrder(
						tuple("note-1", "00"),
						tuple("note-5", "44"));

		insertTransaction();

		List<TransactionDbo> transactionsAfterSaving = transactionRepository
                .findTransactionsDboByDepositsTo_Id("2");

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
                .findTransactionsDboByDepositsTo_Id("non-existing");

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
						.currencyCode("CZK")
						.build())
				.depositsTo(AccountDbo.builder()
						.id("2")
						.currencyCode("EUR")
						.build())
				.withdrawAmount(BigDecimal.ONE)
				.depositAmount(BigDecimal.ONE)
				.conversionRate(1.0)
				.note("note-10")
				.variableSymbol("1010")
				.transactionState(TransactionState.SUCCESSFUL)
				.build();

		transactionRepository.save(transactionDbo);
	}

	private void removeTransaction() {
		transactionRepository.deleteById("7");
	}
}
