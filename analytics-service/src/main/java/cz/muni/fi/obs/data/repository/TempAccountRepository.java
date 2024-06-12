package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.TempAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempAccountRepository extends JpaRepository<TempAccount, Long> {
}
