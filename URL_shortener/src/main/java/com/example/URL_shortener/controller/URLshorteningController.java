package com.example.URL_shortener.controller;

import com.example.URL_shortener.exceptions.AuthorizationErrorException;
import com.example.URL_shortener.exceptions.RedirectTypeException;
import com.example.URL_shortener.exceptions.URLIsNullException;
import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.responses.ShortUrlResponse;
import com.example.URL_shortener.services.AccountService;
import com.example.URL_shortener.services.URLshorteningService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administration")
public class URLshorteningController {
    AccountService accountService;
    URLshorteningService urlshorteningService;

    @Autowired
    public URLshorteningController(AccountService accountService, URLshorteningService urlshorteningService) {
        this.accountService = accountService;
        this.urlshorteningService = urlshorteningService;
    }

    @PostMapping("/short")
    public ResponseEntity<ShortUrlResponse> shortenURL(@RequestHeader String authorization, @RequestBody URLrequest url) {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        // REQUEST HEADER
        // dekripcija tokena
        String encodedAuthorization = authorization.substring(6).trim();
        String decodedAuthorization = new String(Base64.decodeBase64(encodedAuthorization));
        String[] credentials = decodedAuthorization.split(":");

        // provjera autorizacije
        Account account = accountService.checkAuthorization(credentials[0], credentials[1]);
        if (account == null) {
           throw new AuthorizationErrorException("You are not authorized to have access to URL shortening");
        }

        shortUrlResponse = urlshorteningService.generateURL(url);

        URL urlBase = new URL();
        urlBase.setAccountId(credentials[0]);
        urlBase.setUrl(url.getUrl());
        urlBase.setShortenedUrl(shortUrlResponse.getShortUrl());

        urlshorteningService.addURL(urlBase);

        Integer redirectType = url.getRedirectType();

        return new ResponseEntity<>(shortUrlResponse, HttpStatus.valueOf(redirectType)  );
    }

    @ExceptionHandler
    public ResponseEntity<ShortUrlResponse> handleAuthorizationErrorException(AuthorizationErrorException e) {
        ShortUrlResponse error = new ShortUrlResponse();
        error.setDescription(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
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


}


