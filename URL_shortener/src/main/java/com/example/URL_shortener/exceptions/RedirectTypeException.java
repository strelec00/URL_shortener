package com.example.URL_shortener.exceptions;

public class RedirectTypeException extends RuntimeException {
    public RedirectTypeException(String message) {
        super(message);
    }
}
