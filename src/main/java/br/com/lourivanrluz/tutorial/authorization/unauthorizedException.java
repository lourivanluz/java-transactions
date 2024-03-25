package br.com.lourivanrluz.tutorial.authorization;

public class unauthorizedException extends RuntimeException {
    public unauthorizedException(String message) {
        super(message);
    }
}
