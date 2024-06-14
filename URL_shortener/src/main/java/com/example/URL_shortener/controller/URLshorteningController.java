package com.example.URL_shortener.controller;
import com.example.URL_shortener.exceptions.*;
import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.responses.ShortUrlResponse;
import com.example.URL_shortener.services.AccountService;
import com.example.URL_shortener.services.URLshorteningService;
import jakarta.validation.Valid;
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


    @PostMapping(value = "/short", headers = "Authorization", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ShortUrlResponse> shortenURL(@RequestHeader String authorization, @Valid @RequestBody URLrequest url) {

        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();

        urlshorteningService.getRedirectType(url.getRedirectType(), url);

        // REQUEST HEADER

        // Decoding
        String encodedAuthorization = authorization.substring(6).trim();
        String decodedAuthorization = new String(Base64 .decodeBase64(encodedAuthorization));
        String[] credentials = decodedAuthorization.split(":");

        // error handling - password empty
        if (credentials.length != 2 || !authorization.startsWith("Basic ")) {
            throw new HeaderErrorException("Invalid Authorization header value");
        }

        // error handling - provjera autorizacije
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


}


