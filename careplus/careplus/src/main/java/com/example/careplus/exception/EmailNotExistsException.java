package com.example.careplus.exception;

public class EmailNotExistsException extends RuntimeException {
    public EmailNotExistsException(String message) {
        super(message);
    }
}
