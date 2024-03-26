package br.com.lourivanrluz.tutorial.transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface TransactionsFutureRepository extends ListCrudRepository<TransactionFuture, Long> {

    // @Query("SELECT * FROM TRANSACTIONS_FUTURE" +
    // "WHERE is_active = true " +
    // "AND TIMESTAMPDIFF(MINUTE, next_payment, :currentDateTime) <= 1")

    @Query("SELECT * " +
            "FROM TRANSACTIONS_FUTURE AS tf " +
            "INNER JOIN WALLETS AS w ON tf.PAYER = w.ID " +
            "WHERE tf.IS_ACTIVE = true " +
            "AND w.BLOCKED = false " +
            "AND TIMESTAMPDIFF(MINUTE, tf.NEXT_PAYMENT, :currentDateTime) <= 1")
    List<TransactionFuture> findActiveTransactionFutures(LocalDateTime currentDateTime);
}
