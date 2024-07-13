package com.example.URL_shortener.exceptions;

import com.example.URL_shortener.responses.RegisterResponse;
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
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<Object> InternalErrorException(InternalErrorException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
    @ExceptionHandler(AuthorizationErrorException.class)
    public ResponseEntity<ShortUrlResponse> handleAuthorizationErrorException(AuthorizationErrorException e) {
        ShortUrlResponse error = new ShortUrlResponse();
        error.setDescription(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HeaderErrorException.class)
    public ResponseEntity<ShortUrlResponse> handleHeaderErrorException(HeaderErrorException e) {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        shortUrlResponse.setDescription(e.getMessage());
        return new ResponseEntity<>(shortUrlResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RedirectTypeException.class)
    public ResponseEntity<ShortUrlResponse> handleRedirectTypeException(RedirectTypeException ex) {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        shortUrlResponse.setDescription(ex.getMessage());
        return new ResponseEntity<>(shortUrlResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(URLIsNullException.class)
    public ResponseEntity<ShortUrlResponse> handleURLIsNullException(URLIsNullException ex) {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        shortUrlResponse.setDescription(ex.getMessage());
        return new ResponseEntity<>(shortUrlResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ShortUrlResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        StringBuilder description = new StringBuilder();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
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

    @ExceptionHandler(RegisterErrorException.class)
    public ResponseEntity<RegisterResponse> handleRegisterErrorException(RegisterErrorException e) {
        RegisterResponse error = new RegisterResponse(false, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(URLnotFoundException.class)
    public ResponseEntity<String> handleURLnotFound(URLnotFoundException e) {
        return new ResponseEntity<>("URL not found", HttpStatus.NOT_FOUND);
    }

}
