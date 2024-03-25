package br.com.lourivanrluz.tutorial.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

// import java.math.BigDecimal;
// import java.time.LocalDateTime;

// import org.springframework.data.annotation.CreatedDate;
// import org.springframework.data.annotation.Id;
// import org.springframework.data.relational.core.mapping.Table;

// import jakarta.validation.constraints.NotNull;

// @Table("TRANSACTIONS")
// public record Transaction(
//         @Id Long id,
//         @NotNull(message = "payer required field") Long payer,
//         @NotNull(message = "payee required field") Long payee,
//         @NotNull(message = "total amount required field") BigDecimal amount,
//         @NotNull(message = "typeTransaction required field") Integer typeTransaction,
//         @NotNull(message = "installments required field") Integer installments,
//         @CreatedDate LocalDateTime createdAt) {

//     public Transaction {
//         amount = amount.setScale(2);
//     }
// }
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
}
