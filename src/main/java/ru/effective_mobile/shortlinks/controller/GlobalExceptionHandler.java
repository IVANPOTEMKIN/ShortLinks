package ru.effective_mobile.shortlinks.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.effective_mobile.shortlinks.exception.LinkNotFoundException;
import ru.effective_mobile.shortlinks.exception.Violation;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<Violation> handleLinkNotFoundException(LinkNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Violation(LocalDateTime.now(), e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Violation> handleValidationExceptions(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Violation(LocalDateTime.now(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Violation> handleUnexpectedExceptions(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Violation(LocalDateTime.now(), e.getMessage()));
    }
}