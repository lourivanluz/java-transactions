package br.com.lourivanrluz.tutorial.transaction;

public class InvalideTransactionExeption extends RuntimeException {
    public InvalideTransactionExeption(String message) {
        super(message);
    }
}
