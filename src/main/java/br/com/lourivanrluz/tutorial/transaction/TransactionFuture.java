package br.com.lourivanrluz.tutorial.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// @EqualsAndHashCode(callSuper = false)

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "transactions_future")
@Entity
public class TransactionFuture {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal totalAmount;

    private Boolean isActive;

    private Integer numberOfInstallments;

    private LocalDateTime nextPayment;

    @OneToMany(mappedBy = "transactionFuture")
    private List<Transaction> transactions;

    // @OneToMany
    // @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    // private Transaction transaction;

    // public TransactionFuture(Transaction transaction) {
    // this.transaction = transaction;
    // }
    // public TransactionFuture(Long id, Long payer, Long payee, BigDecimal amount,
    // Integer typeTransaction,
    // Integer installments, LocalDateTime createdAt, BigDecimal totalAmount,
    // Boolean isActive,
    // LocalDateTime nextPayment) {
    // super(id, payer, payee, amount, typeTransaction, installments, createdAt);

    // this.totalAmount=totalAmount;this.isActive=isActive;this.nextPayment=nextPayment;
}

// public Transaction convertToTransaction() {
// return new Transaction(null, getPayer(), getPayee(), getAmount(),
// getTypeTransaction(), getInstallments(),
// null);
// }
