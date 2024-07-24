package com.example.URL_shortener.controller;

import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.responses.ShortUrlResponse;
import com.example.URL_shortener.services.AccountService;
import com.example.URL_shortener.services.URLshorteningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(
            description = "Shortens an URL provided in request body with request type. Account should be authorized through Basic Auth header beforehand. If shortening succeeded, response object will contain shortenedUrl: www.xyz.com. If shortening didn't succeed, response will contain description on why it didn't work. User must be authorized!  In API docs, redirection works only if original URL is used with HTTP protocol - example: http://localhost:8080/swagger-ui/index.html#/Redirection/redirect",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @Tag(name = "URL shortening", description = "POST method for URL shortening.")
    @PostMapping(value = "/short", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ShortUrlResponse> shortenURL(
            @Parameter(hidden = true) @RequestHeader("Authorization") String Authorization,
            @Parameter(description = "Original URL that needs to be shortened: ") @Valid @RequestBody URLrequest url) {

        String[] credentials = accountService.authenticate(Authorization);

        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();

        urlshorteningService.getRedirectType(url.getRedirectType(), url);

        shortUrlResponse = urlshorteningService.generateShortURL(url);

        URL urlBase = new URL();
        urlBase.setAccountId(credentials[0]);
        urlBase.setUrl(url.getUrl());
        urlBase.setShortenedUrl(shortUrlResponse.getShortUrl());
        urlBase.setRedirectType(url.getRedirectType());

        urlshorteningService.addURL(urlBase);

        Integer redirectType = url.getRedirectType();

        if (shortUrlResponse.getShortUrl() == null && shortUrlResponse.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(shortUrlResponse);
        }

        return new ResponseEntity<>(shortUrlResponse, HttpStatus.valueOf(redirectType));
    }
}
