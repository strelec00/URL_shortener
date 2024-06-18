package com.example.URL_shortener.controller;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.responses.ShortUrlResponse;
import com.example.URL_shortener.services.AccountService;
import com.example.URL_shortener.services.URLshorteningService;
import jakarta.validation.Valid;
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

        String[] credentials =  accountService.authenticate(authorization);


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


