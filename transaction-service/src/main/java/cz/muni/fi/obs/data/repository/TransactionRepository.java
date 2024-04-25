package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.TransactionDbo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDbo, String> {

	List<TransactionDbo> findTransactionsDbosByWithdrawsFromId(String accountId);

	List<TransactionDbo> findTransactionsDboByDepositsToId(String accountId);

	@Query("SELECT t FROM TransactionDbo t WHERE t.withdrawsFrom.id = :accountId OR t.depositsTo.id = :accountId")
	Page<TransactionDbo> findTransactionHistory(String accountId, Pageable pageable);
}
