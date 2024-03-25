package br.com.lourivanrluz.tutorial.transaction.exeptions;

public class InvalideTransactionExeption extends RuntimeException {
    public InvalideTransactionExeption(String message) {
        super(message);
    }
}
