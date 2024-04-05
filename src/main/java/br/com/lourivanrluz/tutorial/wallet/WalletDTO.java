package br.com.lourivanrluz.tutorial.wallet;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDTO(
        UUID id,
        Integer type,
        BigDecimal balance,
        BigDecimal credit,
        Boolean isBlocked

) {
    public static WalletDTO walletToDto(Wallet wallet) {
        return new WalletDTO(wallet.getId(), wallet.getType(),
                wallet.getBalance(),
                wallet.getCredit(), wallet.getIsBlocked());
    }

}
