package br.com.lourivanrluz.tutorial.transaction.exeptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidBodyRequestExeption extends RuntimeException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(e.getAllErrors().get(0).getDefaultMessage());
    }
}
