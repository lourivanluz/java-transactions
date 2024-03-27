package br.com.lourivanrluz.tutorial.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

// @EqualsAndHashCode(callSuper = false)

@Data
@Table("transactions_future")
public class TransactionFuture extends Transaction {

    @NotNull
    BigDecimal totalAmount;
    @NotNull
    Boolean isActive;

    LocalDateTime nextPayment;

    public TransactionFuture(Long id, Long payer, Long payee, BigDecimal amount, Integer typeTransaction,
            Integer installments, LocalDateTime createdAt, BigDecimal totalAmount, Boolean isActive,
            LocalDateTime nextPayment) {
        super(id, payer, payee, amount, typeTransaction, installments, createdAt);

        this.totalAmount = totalAmount;
        this.isActive = isActive;
        this.nextPayment = nextPayment;
    }

    public Transaction convertToTransaction() {
        return new Transaction(null, getPayer(), getPayee(), getAmount(), getTypeTransaction(), getInstallments(),
                null);
    }

}
