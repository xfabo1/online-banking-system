package cz.muni.fi.obs.data;

import cz.muni.fi.obs.data.dbo.CurrencyDimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyDimension, String> {
}
