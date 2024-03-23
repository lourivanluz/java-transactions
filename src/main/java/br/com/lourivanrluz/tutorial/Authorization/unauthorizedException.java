package br.com.lourivanrluz.tutorial.Authorization;

public class unauthorizedException extends RuntimeException {
    public unauthorizedException(String message) {
        super(message);
    }
}
