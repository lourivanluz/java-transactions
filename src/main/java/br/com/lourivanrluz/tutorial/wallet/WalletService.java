package br.com.lourivanrluz.tutorial.wallet;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    Logger logger = LoggerFactory.getLogger(WalletService.class);

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet HandlerCredit(UUID id, BigDecimal value) {
        logger.info("recebeu o valor de id = ", id);
        Wallet wallet = walletRepository.findById(id).get();
        logger.info("wallet ===", wallet);
        wallet.addCredit(value);
        return walletRepository.save(wallet);
    }

}
