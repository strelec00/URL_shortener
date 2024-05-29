package com.example.URL_shortener.controller;

import com.example.URL_shortener.model.Account;
import com.example.URL_shortener.service.AuthorizationService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administration")
public class URLshorteningController {
    AuthorizationService authorizationService;

    public URLshorteningController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/short")
    public boolean shortenURL(@RequestHeader String authorization) {
        // dekripcija tokena
        String encodedAuthorization = authorization.substring(6).trim();
        String decodedAuthorization = new String(Base64.decodeBase64(encodedAuthorization));
        String[] credentials = decodedAuthorization.split(":");

        // provjera autorizacije
        Account account = authorizationService.checkAuthorization(credentials[0], credentials[1]);
        if (account == null) {
            // TODO
            // throw new AuthorizationErrorReponse("You are not authorized to shorten this URL");
            return false;
        }

        return true;
    }
}
