package br.com.lourivanrluz.tutorial.transaction;

import org.springframework.web.bind.annotation.RestController;

import br.com.lourivanrluz.tutorial.transaction.exeptions.InvalideTransactionExeption;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("transaction")
public class TransationController {
    private final TransactionService transactionService;
    private final Logger LOGGER = LoggerFactory.getLogger(TransationController.class);

    public TransationController(TransactionService transactionService) {
        this.transactionService = transactionService;

    }

    @PostMapping()
    public Transaction transaction(@RequestBody @Valid Transaction transaction) {
        if (transaction.getTypeTransaction() == TransactionType.PaymentinFull.getValue()
                && transaction.getInstallments() == 0) {
            try {
                LOGGER.info("PAGAMENTO A VISTA {}", transaction);
                return transactionService.createFullTransaction(transaction);

            } catch (Exception e) {
                throw new InvalideTransactionExeption(e.getMessage());
            }

        } else if (transaction.getTypeTransaction() == TransactionType.PaymentinInstallments.getValue()
                && transaction.getInstallments() >= 2) {
            try {
                LOGGER.info("PAGAMENTO PARCELADO {}", transaction);
                return transactionService.createInstallmentTransaction(transaction);

            } catch (Exception e) {
                throw new InvalideTransactionExeption(e.getMessage());
            }

        } else {
            throw new InvalideTransactionExeption("Invalid fields for transaction");
        }

    }

    @GetMapping
    public List<Transaction> list() {
        return transactionService.list();
    }
}
