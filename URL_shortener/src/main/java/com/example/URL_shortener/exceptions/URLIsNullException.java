package com.example.URL_shortener.exceptions;

public class URLIsNullException extends RuntimeException {
    public URLIsNullException(String message) {
        super(message);
    }
}
