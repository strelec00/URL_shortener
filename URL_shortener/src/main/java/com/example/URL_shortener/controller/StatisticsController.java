package com.example.URL_shortener.controller;
import com.example.URL_shortener.services.AccountService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/administration")
public class StatisticsController {

    AccountService accountService;

    public StatisticsController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/statistics", headers = "Authorization", consumes = "application/json", produces = "application/json")
    public String GetStatistics(@RequestHeader String authorization) {

        String[] credentials = accountService.authenticate(authorization);



        return "";
    }
}
