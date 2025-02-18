package br.com.lourivanrluz.tutorial.wallet;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("credit")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<WalletDTO> putMethodName(@PathVariable UUID id, @RequestBody Map<String, BigDecimal> body) {
        WalletDTO response = WalletDTO.walletToDto(walletService.HandlerCredit(id, body.get("value")));
        return ResponseEntity.ok().body(response);
    }

}
