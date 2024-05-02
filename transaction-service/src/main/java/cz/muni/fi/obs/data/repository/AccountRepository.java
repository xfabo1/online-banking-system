package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.AccountDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountDbo, String> {
	Optional<AccountDbo> findAccountDboByCustomerId(String customerId);
	List<AccountDbo> findAccountDbosByCustomerId(String customerId);
	List<AccountDbo> findAllByCurrencyCode(String currencyCode);
}
