package com.example.URL_shortener.exceptions;

public class RegisterErrorException extends RuntimeException {
    public RegisterErrorException(String message) {
        super(message);
    }
}
