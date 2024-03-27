package br.com.lourivanrluz.tutorial.schedule;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lourivanrluz.tutorial.transaction.Transaction;
import br.com.lourivanrluz.tutorial.transaction.TransactionFuture;
import br.com.lourivanrluz.tutorial.transaction.TransactionsFutureRepository;
import br.com.lourivanrluz.tutorial.transaction.TransactionsRepository;
import br.com.lourivanrluz.tutorial.transaction.exeptions.InvalideTransactionExeption;
import br.com.lourivanrluz.tutorial.wallet.Wallet;
import br.com.lourivanrluz.tutorial.wallet.WalletRepository;
import br.com.lourivanrluz.tutorial.wallet.WalletType;

@Service
@EnableScheduling
public class ScheduleService {
    private final WalletRepository walletRepository;
    private final TransactionsRepository transactionsRepository;
    private final TransactionsFutureRepository transactionsFutureRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

    public ScheduleService(WalletRepository walletRepository, TransactionsRepository transactionsRepository,
            TransactionsFutureRepository transactionsFutureRepository) {
        this.transactionsFutureRepository = transactionsFutureRepository;
        this.transactionsRepository = transactionsRepository;
        this.walletRepository = walletRepository;
    }

    @Scheduled(fixedRateString = "${SEARCH_INTERVAL_FOR_FUTURE_TRANSACTIONS_IN_MS}") // 5mim = 300000
    public void scheduleTransactionService() {
        List<TransactionFuture> transactionFutureList = transactionsFutureRepository
                .findActiveTransactionFutures(LocalDateTime.now());

        // List<TransactionFuture> transactionFutureList =
        // transactionsFutureRepository.findAll();
        LOGGER.info("***buscou por todas as transaction vancidas hj {}", transactionFutureList);

        for (TransactionFuture transactionFuture : transactionFutureList) {
            scheduleTransaction(transactionFuture);
        }
    }

    @Transactional
    public void scheduleTransaction(TransactionFuture transactionFuture) {
        validate(transactionFuture);
        Wallet payer = walletRepository.findById(transactionFuture.getPayer()).get();
        Wallet payee = walletRepository.findById(transactionFuture.getPayee()).get();
        Wallet savedPayer = walletRepository.save(payer.subBalance(transactionFuture.getAmount()));
        walletRepository.save(payee.addBalance(transactionFuture.getAmount()));
        Transaction savedTransaction = transactionFuture.convertToTransaction();
        Integer newInstallment = transactionFuture.getInstallments() - 1;

        if (newInstallment == 0) {
            transactionFuture.setIsActive(false);
            walletRepository.save(savedPayer.addCredit(transactionFuture.getTotalAmount()));
        } else {
            transactionFuture.setNextPayment(LocalDateTime.now().plusMinutes(2));
        }
        transactionFuture.setInstallments(newInstallment);
        transactionsFutureRepository.save(transactionFuture);
        transactionsRepository.save(savedTransaction);

        LOGGER.info("****REALIZOU PAGAMENTO FUTURO**** {}", savedTransaction);

    }

    private void validate(TransactionFuture transactionFuture) {
        walletRepository.findById(transactionFuture.getPayee())
                .map(payee -> walletRepository.findById(transactionFuture.getPayer())
                        .map(payer -> isTransactionValided(transactionFuture, payer) ? transactionFuture : null)
                        .orElseThrow(() -> new InvalideTransactionExeption(
                                "Invalid transaction - %s".formatted(transactionFuture))))
                .orElseThrow(
                        () -> new InvalideTransactionExeption("Invalid transaction - %s".formatted(transactionFuture)));
    }

    private boolean isTransactionValided(Transaction transaction, Wallet payer) {
        Boolean isValid = (!payer.blocked() && payer.type() == WalletType.COMUM.getValue() &&
                !payer.id().equals(transaction.getPayee()) && transaction.getInstallments() > 0);

        if (payer.balance().compareTo(transaction.getAmount()) <= 0) {
            Wallet blockedWalllet = payer.blockWallet();
            walletRepository.save(blockedWalllet);
            return false;
        }
        return isValid;
    }
}
