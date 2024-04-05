package br.com.lourivanrluz.tutorial.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.lourivanrluz.tutorial.wallet.WalletRepository;
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
    TransactionType typeTransaction;
    @NotNull(message = "Field installments is requered")
    Integer installments;

    LocalDateTime createdAt;

    public TransactionDto(UUID id,
            UUID payer,
            UUID payee,
            BigDecimal amount,
            TransactionType typeTransaction,
            Integer installments, LocalDateTime createdAt) {
        this.id = id;
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
        this.typeTransaction = typeTransaction;
        this.installments = installments;
        this.createdAt = createdAt;
    }

    public Transaction convertToTransaction(TransactionDto transactiondto, WalletRepository walletRepository) {
        return new Transaction(transactiondto.getId(), walletRepository.findById(this.payer).get(),
                walletRepository.findById(this.payee).get(),
                transactiondto.getAmount(), transactiondto.getTypeTransaction(),
                transactiondto.getInstallments(),
                transactiondto.getCreatedAt());
    }

    public static TransactionDto convertTransactionToDto(Transaction transaction) {
        return new TransactionDto(transaction.getId(), transaction.getPayer().getId(),
                transaction.getPayee().getId(),
                transaction.getAmount(), transaction.getTypeTransaction(),
                transaction.getInstallments(),
                transaction.getCreatedAt());
    }

}
