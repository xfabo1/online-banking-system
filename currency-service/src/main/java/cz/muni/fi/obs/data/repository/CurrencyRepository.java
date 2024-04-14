package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {

    Optional<Currency> findByCode(String code);

    Page<Currency> findAllPaged(Pageable pageable);
}
