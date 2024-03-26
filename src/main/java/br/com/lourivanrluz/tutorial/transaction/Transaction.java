package br.com.lourivanrluz.tutorial.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Table("TRANSACTIONS")
public class Transaction {
    @Id
    private Long id;

    @NotNull
    private Long payer;

    @NotNull
    private Long payee;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Integer typeTransaction;

    @NotNull
    private Integer installments;

    @CreatedDate
    LocalDateTime createdAt;

    public Transaction(Long id, Long payer, Long payee, BigDecimal amount, Integer typeTransaction,
            Integer installments, LocalDateTime createdAt) {

        this.id = id;
        this.payer = payer;
        this.payee = payee;
        this.amount = amount.setScale(2);
        this.typeTransaction = typeTransaction;
        this.installments = installments;
        this.createdAt = createdAt;
    }

    public TransactionFuture convertToTransactionFuture() {
        return new TransactionFuture(null, getPayer(), getPayee(), getAmount(), getTypeTransaction(), getInstallments(),
                null, getAmount(), null, null);
    }

}
