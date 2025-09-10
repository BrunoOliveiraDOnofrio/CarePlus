package com.example.careplus.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailNotExistsException.class)
    public ResponseEntity<String> handleEmailNotExistsException(EmailNotExistsException ex){
        return ResponseEntity.status(404).body(ex.getMessage());
    }
    @ExceptionHandler
    public ResponseEntity<String> handleUserNotExistsException(UserNotExistsException e){
        return ResponseEntity.status(404).body(e.getMessage());
    }
}