package cz.muni.fi.obs.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.muni.fi.obs.data.dbo.AccountDbo;

@Repository
public interface AccountRepository extends JpaRepository<AccountDbo, String> {

	Optional<AccountDbo> findAccountDboByAccountNumber(String accountNumber);
}
