package com.example.URL_shortener.exceptions;

public class UrlFormatErrorException extends RuntimeException {
    public UrlFormatErrorException(String message) {
        super(message);
    }
}
