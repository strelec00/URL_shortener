package com.example.URL_shortener.controller;

import com.example.URL_shortener.model.Account;
import com.example.URL_shortener.service.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administration")
public class AccountController {

    AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public String registerAccount(@RequestBody Account account) {
        accountService.createAccount(account);
        return "Account registered successfully";
    }
}
