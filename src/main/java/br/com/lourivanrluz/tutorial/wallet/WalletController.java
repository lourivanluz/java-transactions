package br.com.lourivanrluz.tutorial.wallet;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lourivanrluz.tutorial.transaction.exeptions.ExeptionsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Credito", description = "Endpoint de ADM para adicionar credito a uma wallet")
@RequestMapping("credit")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }
    @Operation(summary = "Adiciona credito", description = "Usado para adicionar credito a uma wallet")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "credito adicionado com sucesso"), 
        @ApiResponse(responseCode = "403", description = "Acesso negado", 
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500" , description = "erro interno do servidor",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Requisição inválida", 
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = ExeptionsDto.class),
                examples = @ExampleObject(value = "{ \"message\": \"Campo 'value' é obrigatório\" }"))),
    })
    @PutMapping("/{id}")
    public ResponseEntity<WalletDTO> putMethodName(@PathVariable UUID id,
    @RequestBody walletBodyDto body) {
        WalletDTO response = WalletDTO.walletToDto(walletService.HandlerCredit(id, body.value()));
        return ResponseEntity.ok().body(response);
    }

}