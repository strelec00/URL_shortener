package com.example.URL_shortener.controller;
import com.example.URL_shortener.services.AccountService;
import com.example.URL_shortener.services.URLshorteningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/administration")
public class StatisticsController {

    AccountService accountService;
    URLshorteningService urlshorteningService;

    @Autowired
    public StatisticsController(AccountService accountService, URLshorteningService urlshorteningService) {
        this.accountService = accountService;
        this.urlshorteningService = urlshorteningService;
    }

    @Operation(description = "Shows an URL statistics of authorized account. Account should be authorized through Basic Auth header beforehand. If statistics works, response object will contain list of urls and how many times have they been shortened url: www.google.com/Hs31SA : 3. If statistics didn't work, response will contain description on why it didn't work. User must be authorized!")
    @Tag(name = "URL statistics", description = "GET method for reading URL statistics.")
    @GetMapping("/statistics")
    @SecurityRequirement(name = "basicAuth")
    public Map<String, Integer> GetStatistics(@Parameter(hidden = true) @RequestHeader("Authorization") String authorization) {

        String[] credentials = accountService.authenticate(authorization);

        return urlshorteningService.findAllByAccountId(credentials[0]);

    }
}
