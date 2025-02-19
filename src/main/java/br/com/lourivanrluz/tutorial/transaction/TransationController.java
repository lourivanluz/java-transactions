package br.com.lourivanrluz.tutorial.transaction;

import org.springframework.web.bind.annotation.RestController;

import br.com.lourivanrluz.tutorial.transaction.exeptions.ExeptionsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Transactions", description = "Endpoints para criar e listar transações")
public class TransationController {
    private final TransactionService transactionService;

    private final Logger LOGGER = LoggerFactory.getLogger(TransationController.class);

    public TransationController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @Operation(summary = "Cria uma transação", description = "cria e retorna uma transação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "transação efetuada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado", 
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500" , description = "erro interno do servidor",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Requisição inválida", 
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = ExeptionsDto.class),
                examples = @ExampleObject(value = "{ \"message\": \"Campo 'amount' é obrigatório\" }"))),
    })
    @PostMapping
    public ResponseEntity<TransactionDto> transaction(@RequestBody @Valid TransactionDto transactionDto) {

        Transaction transaction = transactionDto.convertToTransaction(transactionDto);
        LOGGER.info("transaction foi transformada {}", transaction);
        TransactionDto response = TransactionDto
                .convertTransactionToDto(transactionService.createTransaction(transaction, true));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "lista as transações", description = "retorna uma lista de transações")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "lista de transações retornada com sucesso"),
        @ApiResponse(responseCode = "403" , description = "não autorizado",
        content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500" , description = "erro interno do servidor",
        content = @Content(mediaType = "application/json")),
    })
    @GetMapping
    public List<Transaction> list() {
        return transactionService.list();
    }
}
