package com.example.URL_shortener.exceptions;

import com.example.URL_shortener.responses.ShortUrlResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalUrlExceptionHandler {

    @ExceptionHandler(AuthorizationErrorException.class)
    public ResponseEntity<ShortUrlResponse> handleAuthorizationErrorException(AuthorizationErrorException e) {
        ShortUrlResponse error = new ShortUrlResponse();
        error.setDescription(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(HeaderErrorException.class)
    public ResponseEntity<ShortUrlResponse> handleException(HeaderErrorException e) {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        shortUrlResponse.setDescription(e.getMessage());

        return new ResponseEntity<>(shortUrlResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RedirectTypeException.class)
    public ResponseEntity<ShortUrlResponse> handleException(RedirectTypeException ex) {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        shortUrlResponse.setDescription(ex.getMessage());

        return new ResponseEntity<>(shortUrlResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(URLIsNullException.class)
    public ResponseEntity<ShortUrlResponse> handleException(URLIsNullException ex) {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        shortUrlResponse.setDescription(ex.getMessage());

        return new ResponseEntity<>(shortUrlResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ShortUrlResponse> handleException(MethodArgumentNotValidException ex) {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        StringBuilder description = new StringBuilder();

        for(FieldError error : ex.getBindingResult().getFieldErrors()) {
            description.append(error.getDefaultMessage()).append("; ");
        }

        shortUrlResponse.setDescription(description.toString().trim());

        return new ResponseEntity<>(shortUrlResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ShortUrlResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        shortUrlResponse.setDescription("Invalid JSON format");

        return new ResponseEntity<>(shortUrlResponse, HttpStatus.BAD_REQUEST);
    }
}
