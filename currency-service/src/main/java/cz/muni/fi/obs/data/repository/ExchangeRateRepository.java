package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, String> {

    // fixme: temporary solution, create a better solution that traverses exchange rates and bridges gap between not intermediatelly
    // exchangable currencies
    // ex:
    // If I have exchange rate from euro to yuan and euro to usd, I should be able to exchange yuan to usd by converting to euro and then to usd
    @Query("SELECT ex FROM ExchangeRate ex WHERE ex.from=?1 and ex.to=?2 and ex.validUntil > NOW()")
    Optional<ExchangeRate> findCurrentExchangeRate(String fromId, String toId);
}
