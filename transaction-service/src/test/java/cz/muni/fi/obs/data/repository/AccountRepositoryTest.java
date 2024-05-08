package cz.muni.fi.obs.data.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import cz.muni.fi.obs.data.dbo.AccountDbo;

@Sql(value = { "/initialize_db.sql" }, executionPhase = BEFORE_TEST_CLASS)
@Sql(value = { "/drop_all.sql" }, executionPhase = AFTER_TEST_CLASS)
@DataJpaTest
@ActiveProfiles("test")
public class AccountRepositoryTest {

	@Autowired
	private AccountRepository accountRepository;

	@Test
	public void getAccountByAccountNumber_AccountFound_ReturnsAccount() {
		Optional<AccountDbo> account = accountRepository.findById("1");

		assertThat(account).isPresent();
		assertThat(account.get())
				.returns("1", AccountDbo::getId)
				.returns("CZK", AccountDbo::getCurrencyCode)
				.returns("customer-1", AccountDbo::getCustomerId);
	}

	@Test
	public void getAccountByAccountNumber_AccountNotFound_ReturnsOptionalEmpty() {
		Optional<AccountDbo> account = accountRepository.findById("0");

		assertThat(account).isEmpty();
	}

	@Test
	public void getAccountByCustomerId_AccountFound_ReturnsAccount() {
		Optional<AccountDbo> account = accountRepository.findAccountDboByCustomerId("customer-1");

		assertThat(account).isPresent();
		assertThat(account.get())
				.returns("1", AccountDbo::getId)
				.returns("CZK", AccountDbo::getCurrencyCode)
				.returns("customer-1", AccountDbo::getCustomerId);
	}

	@Test
	public void getAccountByCustomerId_AccountNotFound_ReturnsOptionalEmpty() {
		Optional<AccountDbo> account = accountRepository.findAccountDboByCustomerId("non-existing");

		assertThat(account).isEmpty();
	}

	@Test
	public void getAccountsByCurrencyCode_AccountsFound_ReturnsAccounts() {
		var accounts = accountRepository.findAllByCurrencyCode("CZK");

		assertThat(accounts).hasSize(2)
				.extracting(AccountDbo::getId)
				.containsExactlyInAnyOrder("1", "4");
	}

	@Test
	public void getAccountsByCurrencyCode_AccountsNotFound_ReturnsEmptyList() {
		var accounts = accountRepository.findAllByCurrencyCode("non-existing");

		assertThat(accounts).isEmpty();
	}
}
