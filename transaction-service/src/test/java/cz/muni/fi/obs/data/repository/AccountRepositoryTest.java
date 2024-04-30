package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.AccountDbo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@Sql(value = { "/initialize_db.sql" }, executionPhase = BEFORE_TEST_CLASS)
@Sql(value = { "/drop_all.sql" }, executionPhase = AFTER_TEST_CLASS)
@DataJpaTest
@ActiveProfiles("test")
public class AccountRepositoryTest {

	@Autowired
	private AccountRepository accountRepository;

	@Test
	public void getAccountByAccountNumber_AccountFound_ReturnsAccount() {
		Optional<AccountDbo> account = accountRepository.findAccountDboByAccountNumber("account-1");

		assertThat(account).isPresent();
		assertThat(account.get())
				.returns("1", AccountDbo::getId)
				.returns("account-1", AccountDbo::getAccountNumber)
				.returns("CZK", AccountDbo::getCurrencyCode)
				.returns("customer-1", AccountDbo::getCustomerId);
	}

	@Test
	public void getAccountByAccountNumber_AccountNotFound_ReturnsOptionalEmpty() {
		Optional<AccountDbo> account = accountRepository.findAccountDboByAccountNumber("non-existing");

		assertThat(account).isEmpty();
	}

	@Test
	public void getAccountByCustomerId_AccountFound_ReturnsAccount() {
		Optional<AccountDbo> account = accountRepository.findAccountDboByCustomerId("customer-1");

		assertThat(account).isPresent();
		assertThat(account.get())
				.returns("1", AccountDbo::getId)
				.returns("account-1", AccountDbo::getAccountNumber)
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
				.extracting(AccountDbo::getAccountNumber)
				.containsExactlyInAnyOrder("account-1", "account-4");
	}

	@Test
	public void getAccountsByCurrencyCode_AccountsNotFound_ReturnsEmptyList() {
		var accounts = accountRepository.findAllByCurrencyCode("non-existing");

		assertThat(accounts).isEmpty();
	}
}
