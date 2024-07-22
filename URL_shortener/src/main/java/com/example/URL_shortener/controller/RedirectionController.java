package com.example.URL_shortener.controller;

import com.example.URL_shortener.exceptions.URLnotFoundException;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.services.URLshorteningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class RedirectionController {

    private final URLshorteningService urlshorteningService;

    public RedirectionController(URLshorteningService urlshorteningService) {
        this.urlshorteningService = urlshorteningService;
    }

    @Operation(description = "Redirects user from shortened URL to original URL. If works, user will be redirected to original Url. If it doesn't work, user will get error url not found. In API docs, redirection works only if original URL is used with HTTP protocol.")
    @Tag(name = "Redirection", description = "GET method for redirecting URLs.")
    @GetMapping("/{hash}")
    public ResponseEntity<?> redirect(
            @Parameter(description = "Hash used for redirecting to the original url: ") @PathVariable String hash) {

        String generatedUrl = urlshorteningService.generateURL();
        String shortUrl = generatedUrl + hash;

        URL redirectUrl = urlshorteningService.getURLbyShortUrl(shortUrl);

        if (redirectUrl == null) {
            throw new URLnotFoundException("URL not found for hash: " + hash);
        }

        return ResponseEntity.status(redirectUrl.getRedirectType())
                .location(URI.create(redirectUrl.getUrl()))
                .build();
    }
}