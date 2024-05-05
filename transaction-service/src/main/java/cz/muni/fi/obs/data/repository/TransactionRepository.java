package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.TransactionDbo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.Instant;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDbo, String> {

	List<TransactionDbo> findTransactionsDboByWithdrawsFrom_Id(String accountId);

	List<TransactionDbo> findTransactionsDboByDepositsTo_Id(String accountId);

	@Query("SELECT t FROM TransactionDbo t WHERE t.withdrawsFrom.id = :accountId OR t.depositsTo.id = :accountId")
	Page<TransactionDbo> findTransactionHistory(String accountId, Pageable pageable);


	@Query("SELECT t FROM TransactionDbo t WHERE (t.withdrawsFrom.id = :accountId OR t.depositsTo.id = :accountId) " +
			"AND (:startOfDay IS NULL OR t.transactionTime >= :startOfDay) AND (:endOfDay IS NULL OR t.transactionTime <= :endOfDay)")
	Page<TransactionDbo> listTransactions(String accountId, Pageable pageable, Instant startOfDay, Instant endOfDay);
}
