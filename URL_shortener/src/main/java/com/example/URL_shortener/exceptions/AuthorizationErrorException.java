package com.example.URL_shortener.exceptions;

public class AuthorizationErrorException extends RuntimeException {
    public AuthorizationErrorException(String message) {
        super(message);
    }
}
