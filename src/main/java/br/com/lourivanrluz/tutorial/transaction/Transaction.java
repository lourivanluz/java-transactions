package br.com.lourivanrluz.tutorial.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import br.com.lourivanrluz.tutorial.wallet.Wallet;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "transactions")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Wallet payer;

    @ManyToOne
    private Wallet payee;

    private BigDecimal amount;

    @Enumerated(EnumType.ORDINAL)
    private TransactionType typeTransaction;

    private Integer installments;

    @CreatedDate
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "transaction_future_id", referencedColumnName = "id")
    private TransactionFuture transactionFuture;

    public Transaction(UUID id, Wallet payer, Wallet payee, BigDecimal amount, TransactionType typeTransaction,
            Integer installments, LocalDateTime createdAt) {
        this.id = id;
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
        this.typeTransaction = typeTransaction;
        this.installments = installments;
        this.createdAt = createdAt;
    }
}
