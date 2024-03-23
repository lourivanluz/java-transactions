package br.com.lourivanrluz.tutorial.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TransactionExceptionHandler {
    @ExceptionHandler(InvalideTransactionExeption.class)
    public ResponseEntity<Object> handle(InvalideTransactionExeption e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
