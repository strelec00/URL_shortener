package com.example.URL_shortener.controller;
import com.example.URL_shortener.services.AccountService;
import com.example.URL_shortener.services.URLshorteningService;
import jakarta.validation.Valid;
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

    @GetMapping("/statistics")
    public Map<String, Integer> GetStatistics(@RequestHeader String authorization) {

        String[] credentials = accountService.authenticate(authorization);

        return urlshorteningService.findAllByAccountId(credentials[0]);

    }
}
