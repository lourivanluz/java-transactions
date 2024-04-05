package br.com.lourivanrluz.tutorial.mailSend;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.com.lourivanrluz.tutorial.transaction.Transaction;
import br.com.lourivanrluz.tutorial.transaction.TransactionDto;
import br.com.lourivanrluz.tutorial.wallet.Wallet;

public class Email {
    private String to;
    private String subject;
    private String body;

    public Email() {
    }

    public Email(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public Email(String emailAsString) {
        String[] parts = emailAsString.split(";");
        if (parts.length == 3) {
            this.to = parts[0];
            this.subject = parts[1];
            this.body = parts[2];
        } else {
            throw new IllegalArgumentException("Invalid email string format: " + emailAsString);
        }
    }

    public Email(Transaction transaction) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String name = transaction.getPayer().getUser().getFullName();
        String email = transaction.getPayer().getUser().getEmail();
        BigDecimal value = transaction.getAmount();
        String data = LocalDateTime.now().format(formatter).toString();

        String message = """
                Mr. %s,
                your purchase made on the
                %s for the amount of $%.2f cash
                has been successfully approved.
                """.formatted(name, data,
                value);
        this.to = email;
        this.subject = "Transaction accept";
        this.body = message;
    }

    @Override
    public String toString() {
        return to + ";" + subject + ";" + body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}