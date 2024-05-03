package cz.muni.fi.obs.data;

import cz.muni.fi.obs.data.dbo.CurrencyDimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyDimension, String> {
    @Query("SELECT c FROM CurrencyDimension c WHERE c.symbol = :currencyCode")
    Optional<CurrencyDimension> findByCurrencyCode(String currencyCode);
}
