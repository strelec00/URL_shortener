package com.example.URL_shortener.controller;

import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.services.URLshorteningService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
public class RedirectionController {

    URLshorteningService urlshorteningService;

    public RedirectionController(URLshorteningService urlshorteningService) {
        this.urlshorteningService = urlshorteningService;
    }


    @GetMapping("/{hash}")
    public ResponseEntity<Void> redirect(@PathVariable String hash) {
        String generatedUrl = urlshorteningService.generateURL();
        String ShortUrl = generatedUrl + hash;

        URL redirectUrl = urlshorteningService.getURLbyShortUrl(ShortUrl);

        return ResponseEntity.status(redirectUrl.getRedirectType())
                .location(URI.create(redirectUrl.getUrl()))
                .build();

    }
}
