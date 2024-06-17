package com.example.URL_shortener.exceptions;

public class HeaderFormatErrorException extends RuntimeException{
    public HeaderFormatErrorException(String message) {
        super(message);
    }
}
