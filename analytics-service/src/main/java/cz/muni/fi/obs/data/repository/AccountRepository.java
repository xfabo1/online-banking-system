package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.AccountDimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountDimension, String>{
}
