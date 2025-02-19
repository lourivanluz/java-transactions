package br.com.lourivanrluz.tutorial.wallet;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record WalletDTO(
        UUID id,
        @Schema(example = "1")
        Integer type,
        @Schema(example = "1000")
        BigDecimal balance,
        @Schema(example = "500")
        BigDecimal credit,
        @Schema(example = "false")
        Boolean isBlocked

) {
    public static WalletDTO walletToDto(Wallet wallet) {
        return new WalletDTO(wallet.getId(), wallet.getType(),
                wallet.getBalance(),
                wallet.getCredit(), wallet.getIsBlocked());
    }

}
