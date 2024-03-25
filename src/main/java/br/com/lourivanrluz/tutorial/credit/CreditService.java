package br.com.lourivanrluz.tutorial.credit;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.com.lourivanrluz.tutorial.wallet.Wallet;
import br.com.lourivanrluz.tutorial.wallet.WalletRepository;

@Service
public class CreditService {
    private WalletRepository walletRepository;

    public CreditService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet HandlerCredit(long id, BigDecimal value) {
        Wallet wallet = walletRepository.findById(id).get();
        return walletRepository.save(wallet.addCredit(value));
    }
}
