package br.com.lourivanrluz.tutorial.transaction;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lourivanrluz.tutorial.Authorization.AuthorizerService;
import br.com.lourivanrluz.tutorial.notification.NotificationService;
import br.com.lourivanrluz.tutorial.wallet.Wallet;
import br.com.lourivanrluz.tutorial.wallet.WalletRepository;
import br.com.lourivanrluz.tutorial.wallet.WalletType;

@Service
public class TransactionService {
    private final TransactionsRepository transactionsRepository;
    private final WalletRepository walletRepository;
    private final AuthorizerService authorizerService;
    private final NotificationService notificationService;

    public TransactionService(
            TransactionsRepository transactionsRepository,
            WalletRepository walletRepository,
            AuthorizerService authorizerService,
            NotificationService notificationService) {
        this.transactionsRepository = transactionsRepository;
        this.walletRepository = walletRepository;
        this.authorizerService = authorizerService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {

        // 1 validar
        validate(transaction);
        // 2 criar a trasacao
        var newTransaction = transactionsRepository.save(transaction);
        // 3 debitar da carteira
        var walletPayer = walletRepository.findById(transaction.payer()).get();
        walletRepository.save(walletPayer.debet(transaction.value()));
        // 4 creditar na carteira
        var walletPayee = walletRepository.findById(transaction.payee()).get();
        walletRepository.save(walletPayee.credit(transaction.value()));

        // 5 autorizar
        authorizerService.authorize(transaction);
        // 6 notificar
        notificationService.notify(transaction);

        return newTransaction;
    }
    // se o pagador é do tipo comum
    // se o pagador temo dinheiro na carteira
    // se o pagador for diferente do recebedor

    // entender melhor depois
    private void validate(Transaction transaction) {
        walletRepository.findById(transaction.payee())
                .map(payee -> walletRepository.findById(transaction.payer())
                        .map(payer -> isTransactionValided(transaction, payer) ? transaction : null)
                        .orElseThrow(() -> new InvalideTransactionExeption(
                                "Invalid transaction - %s".formatted(transaction))))
                .orElseThrow(() -> new InvalideTransactionExeption("Invalid transaction - %s".formatted(transaction)));
    }

    private boolean isTransactionValided(Transaction transaction, Wallet payer) {
        return payer.type() == WalletType.COMUM.getValue() &&
                payer.balance().compareTo(transaction.value()) >= 0 &&
                !payer.id().equals(transaction.payee());
    }

    public List<Transaction> list() {
        return transactionsRepository.findAll();
    }
}
