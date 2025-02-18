package br.com.lourivanrluz.tutorial.transaction;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("transaction")
public class TransationController {
    private final TransactionService transactionService;

    private final Logger LOGGER = LoggerFactory.getLogger(TransationController.class);

    public TransationController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDto> transaction(@RequestBody @Valid TransactionDto transactionDto) {

        Transaction transaction = transactionDto.convertToTransaction(transactionDto);
        LOGGER.info("transaction foi transformada {}", transaction);
        TransactionDto response = TransactionDto
                .convertTransactionToDto(transactionService.createTransaction(transaction, true));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<Transaction> list() {
        return transactionService.list();
    }
}
