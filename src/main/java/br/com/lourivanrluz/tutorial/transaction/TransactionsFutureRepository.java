package br.com.lourivanrluz.tutorial.transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import br.com.lourivanrluz.tutorial.transaction.Transaction;

@Repository
public interface TransactionsFutureRepository extends ListCrudRepository<TransactionFuture, UUID> {

    // """
    // SELECT tf
    // FROM TransactionFuture tf
    // JOIN tf.transactions t
    // JOIN t.wallet w
    // WHERE tf.isActive = true
    // AND w.isBlocked = false
    // AND FUNCTION('EXTRACT', EPOCH FROM :currentDateTime - tf.nextPayment) >= 0
    // """

    // """"
    // SELECT tf
    // FROM transactions_future AS tf
    // WHERE tf.is_active = true
    // AND transactions .is_blocked = false
    // AND tf.next_payment < :currentDateTime
    // """"

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
