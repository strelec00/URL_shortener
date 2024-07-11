package com.example.URL_shortener.controller;
import com.example.URL_shortener.exceptions.InternalErrorException;
import com.example.URL_shortener.exceptions.URLnotFoundException;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.services.URLshorteningService;
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
         {
            String generatedUrl = urlshorteningService.generateURL();
            String ShortUrl = generatedUrl + hash;

            URL redirectUrl = urlshorteningService.getURLbyShortUrl(ShortUrl);

            if (redirectUrl == null) {
                throw new URLnotFoundException("URL not found for hash: " + hash);
            }

            return ResponseEntity.status(redirectUrl.getRedirectType())
                    .location(URI.create(redirectUrl.getUrl()))
                    .build();
        }
    }
}
