package br.com.lourivanrluz.tutorial.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lourivanrluz.tutorial.authorization.AuthorizerService;
import br.com.lourivanrluz.tutorial.mailSend.Email;
import br.com.lourivanrluz.tutorial.notification.NotificationService;
import br.com.lourivanrluz.tutorial.transaction.exeptions.InvalideTransactionExeption;
import br.com.lourivanrluz.tutorial.wallet.Wallet;
import br.com.lourivanrluz.tutorial.wallet.WalletRepository;
import br.com.lourivanrluz.tutorial.wallet.WalletType;
import jakarta.transaction.InvalidTransactionException;

@Service
@PropertySource("classpath:application.properties")
public class TransactionService {
    private final TransactionsRepository transactionsRepository;
    private final TransactionsFutureRepository transactionsFutureRepository;
    private final WalletRepository walletRepository;
    private final AuthorizerService authorizerService;
    private final NotificationService notificationService;

    @Autowired
    private Environment env;

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
    public Transaction createTransaction(Transaction transaction, Boolean creatScheduleTransaction) {

        LOGGER.info("-TRANSACTION-{}", transaction.getTypeTransaction().equals(1) ? "FULL" : "INSTALLMENTS");
        // validar
        validateTransaction(transaction);

        // debitar e creditar nas wallwts
        Wallet walletPayer = transaction.getPayer();
        Wallet walletPayee = transaction.getPayee();

        walletPayer.subBalance(transaction.getAmount());
        // Wallet savedPayer = walletRepository.save(walletPayer);

        walletPayee.addBalance(transaction.getAmount());
        walletRepository.save(walletPayee);

        Transaction transactionSaved = transactionsRepository.save(transaction);

        if (creatScheduleTransaction
                && transaction.getTypeTransaction().equals(TransactionType.PaymentinInstallments.getValue())) {

            TransactionFuture transactionFuture = new TransactionFuture();

            BigDecimal totalAmount = transaction.getAmount()
                    .multiply(BigDecimal.valueOf(transaction.getInstallments()));

            walletPayer.subCredit(totalAmount);

            walletRepository.save(walletPayer);

            List<Transaction> transactionsList = transactionFuture.getTransactions();
            transactionsList.add(transactionSaved);
            transactionFuture.setTransactions(transactionsList);

            transactionFuture.setTotalAmount(totalAmount);
            transactionFuture.setIsActive(true);
            transactionFuture.setNumberOfInstallments(transactionSaved.getInstallments() - 1);
            transactionFuture.setNextPayment(getPaymentDeadLineProp(env));

            transactionsFutureRepository.save(transactionFuture);

        }
        TransactionDto transactionDto = TransactionDto.convertTransactionToDto(transactionSaved);

        authorizerService.authorize(transactionSaved);

        if (env.getProperty("SEND_EMAIL").equals("true")) {
            LOGGER.info("-TRANSACTION-ENVIARA-EMAIL}");
            Email mensagem = new Email(transactionSaved);
            notificationService.notify(mensagem.toString());
        } else {
            notificationService.notify(transactionDto.toString());
        }

        return transactionSaved;
    }

    private void validateTransaction(Transaction transaction) {

        if (!isCommonValid(transaction)) {
            throw new InvalideTransactionExeption("Invalid transaction");
        }

        if (transaction.getTypeTransaction().equals(TransactionType.PaymentinInstallments.getValue())) {
            if (!isFutureTransactionValid(transaction)) {
                throw new InvalideTransactionExeption("Invalid transaction");
            }
        }
    }

    private boolean isCommonValid(Transaction transaction) {
        Wallet payer = transaction.getPayer();
        BigDecimal amount = transaction.getAmount();

        return !payer.getIsBlocked() &&
                payer.getType() == WalletType.COMUM.getValue() &&
                payer.getBalance().compareTo(amount) >= 0 &&
                !payer.getId().equals(transaction.getPayee().getId());
    }

    private boolean isFutureTransactionValid(Transaction transaction) {
        Wallet payer = transaction.getPayer();
        BigDecimal amount = transaction.getAmount();
        int installments = transaction.getInstallments();
        BigDecimal totalAmount = amount.multiply(BigDecimal.valueOf(installments));
        return payer.getCredit().compareTo(totalAmount) >= 0;
    }

    public static LocalDateTime getPaymentDeadLineProp(Environment env) {
        Integer month = Integer.valueOf(env.getProperty("PAYMENT_DEADLINE_IN_MONTHS"));
        Integer days = Integer.valueOf(env.getProperty("PAYMENT_DEADLINE_IN_DAYS"));
        Integer hours = Integer.valueOf(env.getProperty("PAYMENT_DEADLINE_IN_HOURS"));
        Integer minutes = Integer.valueOf(env.getProperty("PAYMENT_DEADLINE_IN_MINUTES"));

        return LocalDateTime.now().plusMonths(month).plusDays(days).plusHours(hours).plusMinutes(minutes);
    }

    public List<Transaction> list() {
        return transactionsRepository.findAll();
    }
}
