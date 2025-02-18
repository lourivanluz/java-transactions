package br.com.lourivanrluz.tutorial.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.lourivanrluz.tutorial.wallet.Wallet;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionDto {

    UUID id;
    @NotNull(message = "Field Payer is requered")
    UUID payer;
    @NotNull(message = "Field payee is requered")
    UUID payee;
    @NotNull(message = "Field amount is requered")
    BigDecimal amount;
    @NotNull(message = "Field typeTransaction is requered")
    String typeTransaction;
    @NotNull(message = "Field installments is requered")
    Integer installments;

    LocalDateTime createdAt;

    public TransactionDto(UUID id,
            UUID payer,
            UUID payee,
            BigDecimal amount,
            String typeTransaction,
            Integer installments, LocalDateTime createdAt) {
        this.id = id;
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
        this.typeTransaction = typeTransaction;
        this.installments = installments;
        this.createdAt = createdAt;
    }

    public Transaction convertToTransaction(TransactionDto transactiondto) {

        TransactionType type = Integer.valueOf(transactiondto.getTypeTransaction()) == 0 ? TransactionType.PaymentinFull
                : TransactionType.PaymentinInstallments;

        return new Transaction(transactiondto.getId(), new Wallet(transactiondto.getPayer()),
                new Wallet(transactiondto.getPayee()),
                transactiondto.getAmount(), type,
                transactiondto.getInstallments(),
                transactiondto.getCreatedAt());
    }

    public static TransactionDto convertTransactionToDto(Transaction transaction) {
        String type = transaction.getTypeTransaction().getType();
        return new TransactionDto(transaction.getId(), transaction.getPayer().getId(),
                transaction.getPayee().getId(),
                transaction.getAmount(), type,
                transaction.getInstallments(),
                transaction.getCreatedAt());
    }

}
