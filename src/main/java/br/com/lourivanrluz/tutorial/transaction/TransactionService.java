package br.com.lourivanrluz.tutorial.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lourivanrluz.tutorial.authorization.AuthorizerService;
import br.com.lourivanrluz.tutorial.notification.NotificationService;
import br.com.lourivanrluz.tutorial.transaction.exeptions.InvalideTransactionExeption;
import br.com.lourivanrluz.tutorial.wallet.Wallet;
import br.com.lourivanrluz.tutorial.wallet.WalletRepository;
import br.com.lourivanrluz.tutorial.wallet.WalletType;

@Service
public class TransactionService {
    private final TransactionsRepository transactionsRepository;
    private final TransactionsFutureRepository transactionsFutureRepository;
    private final WalletRepository walletRepository;
    private final AuthorizerService authorizerService;
    private final NotificationService notificationService;

    private final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(
            TransactionsRepository transactionsRepository,
            TransactionsFutureRepository transactionsFutureRepository,
            WalletRepository walletRepository,
            AuthorizerService authorizerService,
            NotificationService notificationService) {
        this.transactionsRepository = transactionsRepository;
        this.transactionsFutureRepository = transactionsFutureRepository;
        this.walletRepository = walletRepository;
        this.authorizerService = authorizerService;
        this.notificationService = notificationService;

    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        LOGGER.info("-TRANSACTION-{}", transaction);
        // validar
        validate(transaction);

        LOGGER.info("-TRANSACTION-POS-SAVE {}", transaction);
        // debitar e creditar nas wallwts
        Wallet walletPayer = walletRepository.findById(transaction.getPayer()).get();
        Wallet saved = walletRepository.save(walletPayer.subBalance(transaction.getAmount()));
        Wallet walletPayee = walletRepository.findById(transaction.getPayee()).get();
        walletRepository.save(walletPayee.addBalance(transaction.getAmount()));

        if (transaction instanceof TransactionFuture) {
            LOGGER.info("-TRANSACTIONFUTURE-", transaction.toString());
            BigDecimal totalAmount = transaction.getAmount()
                    .multiply(BigDecimal.valueOf(transaction.getInstallments()));
            walletRepository.save(saved.subCredit(totalAmount));

            LOGGER.info("-TRANSACTIONFUTURE-POS-SAVE-{}", transaction);
        }
        authorizerService.authorize(transaction);
        notificationService.notify(transaction.toString());

        if (transaction instanceof TransactionFuture) {
            TransactionFuture transactionfuture = (TransactionFuture) transaction;
            Transaction result = transactionsRepository.save(transactionfuture.convertToTransaction());
            transactionfuture.setNextPayment(LocalDateTime.now().plusMinutes(1));
            transactionfuture.setInstallments(transactionfuture.getInstallments() - 1);
            transactionsFutureRepository.save(transactionfuture);

            return result;
        }
        return transactionsRepository.save(transaction);
    }

    private void validate(Transaction transaction) {
        walletRepository.findById(transaction.getPayee())
                .map(payee -> walletRepository.findById(transaction.getPayer())
                        .map(payer -> isTransactionValided(transaction, payer) ? transaction : null)
                        .orElseThrow(() -> new InvalideTransactionExeption(
                                "Invalid transaction - %s".formatted(transaction))))
                .orElseThrow(() -> new InvalideTransactionExeption("Invalid transaction - %s".formatted(transaction)));
    }

    private boolean isTransactionValided(Transaction transaction, Wallet payer) {
        Boolean validedCommum = !payer.blocked() && payer.type() == WalletType.COMUM.getValue() &&
                payer.balance().compareTo(transaction.getAmount()) >= 0 &&
                !payer.id().equals(transaction.getPayee());

        if (transaction instanceof TransactionFuture) {
            TransactionFuture transactionFuture = (TransactionFuture) transaction;
            Boolean haveCredit = payer.credit().compareTo(transactionFuture.getTotalAmount()) >= 0;
            return haveCredit && validedCommum;
        }

        return validedCommum;
    }

    public Transaction createFullTransaction(Transaction transaction) {
        return createTransaction(transaction);
    }

    public Transaction createInstallmentTransaction(Transaction transaction) {

        BigDecimal totalAmount = transaction.getAmount()
                .multiply(BigDecimal.valueOf(transaction.getInstallments()));
        TransactionFuture newtransaction = transaction.convertToTransactionFuture();
        newtransaction.setTotalAmount(totalAmount);
        newtransaction.setIsActive(true);
        return createTransaction(newtransaction);

    }

    public List<Transaction> list() {
        return transactionsRepository.findAll();
    }
}
