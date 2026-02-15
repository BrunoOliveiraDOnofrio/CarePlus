package com.example.careplus.exception;

public class MissingFieldException extends RuntimeException {
    public MissingFieldException(String message) {
        super(message);
    }
}
