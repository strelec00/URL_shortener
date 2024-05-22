package com.example.URL_shortener.response;

public class RegisterErrorReponse extends RuntimeException {
    public RegisterErrorReponse(String message) {
        super(message);
    }
}
