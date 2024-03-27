package br.com.lourivanrluz.tutorial.transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface TransactionsFutureRepository extends ListCrudRepository<TransactionFuture, Long> {

    // @Query("SELECT * FROM TRANSACTIONS_FUTURE" +
    // "WHERE is_active = true " +
    // "AND TIMESTAMPDIFF(MINUTE, next_payment, :currentDateTime) <= 1")

    @Query("""
            SELECT tf.*
            FROM transactions_future AS tf
            INNER JOIN wallets AS w ON tf.payer = w.id
            WHERE tf.is_active = true
            AND w."blocked" = false
            AND EXTRACT(EPOCH FROM (:currentDateTime - tf.next_payment)) >=0;
                """)
    List<TransactionFuture> findActiveTransactionFutures(LocalDateTime currentDateTime);
}
