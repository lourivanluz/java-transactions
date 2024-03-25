package br.com.lourivanrluz.tutorial.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// @EqualsAndHashCode(callSuper = false)

@ToString
@Setter
@Getter
@Table("TRANSACTIONS_FUTURE")
public class TransactionFuture extends Transaction {

    @NotNull
    BigDecimal totalAmount;
    @NotNull
    Boolean isActive;

    public TransactionFuture(Long id, Long payer, Long payee, BigDecimal amount, Integer typeTransaction,
            Integer installments, LocalDateTime createdAt, BigDecimal totalAmount, Boolean isActive) {
        super(id, payer, payee, amount, typeTransaction, installments, createdAt);

        this.totalAmount = totalAmount;
        this.isActive = isActive;
    }

}