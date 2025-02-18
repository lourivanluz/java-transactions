package br.com.lourivanrluz.tutorial.transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsFutureRepository extends ListCrudRepository<TransactionFuture, UUID> {
    @Query("""
            SELECT tf
            FROM TransactionFuture tf
            JOIN tf.transactions t
            WHERE tf.isActive = true
            AND t.payer.isBlocked = false
            AND tf.nextPayment < :currentDateTime
                        """)
    List<TransactionFuture> findActiveTransactionFutures(@Param("currentDateTime") LocalDateTime currentDateTime);
}
