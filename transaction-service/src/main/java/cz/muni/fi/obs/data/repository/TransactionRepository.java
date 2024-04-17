package cz.muni.fi.obs.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.muni.fi.obs.data.dbo.TransactionDbo;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDbo, String> {

	List<TransactionDbo> findTransactionsDboByWithdrawsFrom(String withdrawsFrom);
	List<TransactionDbo> findTransactionsDboByDepositsTo(String depositsTo);

	Page<TransactionDbo> findAllById(String accountId, Pageable pageable);
}
