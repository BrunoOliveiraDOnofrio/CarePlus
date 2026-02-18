package com.example.careplus.exception;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler
    public ResponseEntity<String> handlerResourceNotFoundException(ResourceNotFoundException e){
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerUserAlreadyExistsException(UserAlreadyExistsException e){
        return ResponseEntity.status(409).body(e.getMessage());
    }
}