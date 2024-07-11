package com.example.URL_shortener.controller;

import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.models.AccountId;
import com.example.URL_shortener.exceptions.RegisterErrorException;
import com.example.URL_shortener.responses.RegisterResponse;
import com.example.URL_shortener.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/administration")
public class AccountController {

    AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // add acc
    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public RegisterResponse registerAccount(@Valid @RequestBody AccountId accountId) {

        // Throw exc if accountId already exists
        if(accountService.findAccountById(accountId.getAccountId())){
            throw new RegisterErrorException("Account ID already exists!");
        }
        Account account = new Account();
        account.setAccountId(accountId.getAccountId());

        String password = accountService.generateRandomPassword(12);
        account.setPassword(password);

        accountService.createAccount(account);

        return new RegisterResponse(account.getPassword(),true);
    }


}

