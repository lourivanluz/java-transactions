package br.com.lourivanrluz.tutorial.schedule;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lourivanrluz.tutorial.transaction.Transaction;
import br.com.lourivanrluz.tutorial.transaction.TransactionFuture;
import br.com.lourivanrluz.tutorial.transaction.TransactionService;
import br.com.lourivanrluz.tutorial.transaction.TransactionsFutureRepository;
import br.com.lourivanrluz.tutorial.transaction.exeptions.InvalideTransactionExeption;
import br.com.lourivanrluz.tutorial.wallet.Wallet;
import br.com.lourivanrluz.tutorial.wallet.WalletRepository;

@Service
@EnableScheduling
@PropertySource("classpath:application.properties")
public class ScheduleService {
    private final WalletRepository walletRepository;
    private final TransactionsFutureRepository transactionsFutureRepository;
    private final TransactionService transactionservice;
    @Autowired
    private Environment env;
    private final Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

    public ScheduleService(WalletRepository walletRepository,
            TransactionsFutureRepository transactionsFutureRepository, TransactionService transactionservice) {
        this.transactionsFutureRepository = transactionsFutureRepository;
        this.walletRepository = walletRepository;
        this.transactionservice = transactionservice;
    }

    @Scheduled(fixedRateString = "${SEARCH_INTERVAL_FOR_FUTURE_TRANSACTIONS_IN_MS}") // 5mim = 300000
    public void scheduleTransactionService() {
        List<TransactionFuture> transactionFutureList = transactionsFutureRepository
                .findActiveTransactionFutures(LocalDateTime.now());

        LOGGER.info("***buscou por todas as transaction vancidas hj {}", transactionFutureList);

        for (TransactionFuture transactionFuture : transactionFutureList) {
            scheduleTransaction(transactionFuture);
        }
    }

    @Transactional
    public Transaction scheduleTransaction(TransactionFuture transactionFuture) {

        Transaction transaction = transactionFuture.getTransactions()
                .get(transactionFuture.getTransactions().size() - 1);
        transaction.setInstallments(transactionFuture.getNumberOfInstallments());

        Wallet walletPayer = transaction.getPayer();

        try {
            Transaction transactionSuc = transactionservice.createTransaction(transaction, false);
            LOGGER.info("****REALIZOU PAGAMENTO FUTURO**** {}", transactionSuc);
            transactionFuture.setNumberOfInstallments(transactionFuture.getNumberOfInstallments() - 1);
            if (transactionFuture.getNumberOfInstallments() == 0) {
                transactionFuture.setIsActive(false);

                // Wallet walletPayer = walletRepository.findById(transaction.getPayer()).get();
                walletPayer.addCredit(transactionFuture.getTotalAmount());
                walletRepository.save(walletPayer);

            } else {
                transactionFuture.setNextPayment(TransactionService.getPaymentDeadLineProp(env));
            }

            transactionsFutureRepository.save(transactionFuture);
            return transactionSuc;
        } catch (InvalideTransactionExeption e) {

            if (walletPayer.getBalance().compareTo(transaction.getAmount()) <= 0) {
                walletPayer.blockWallet();
                walletRepository.save(walletPayer);
                return transaction;
            }
        }
        return transaction;

    }
}
