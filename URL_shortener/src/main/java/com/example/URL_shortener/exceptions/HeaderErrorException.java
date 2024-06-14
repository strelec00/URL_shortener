package com.example.URL_shortener.exceptions;

public class HeaderErrorException extends RuntimeException {
    public HeaderErrorException(String message) {
        super(message);
    }
}
