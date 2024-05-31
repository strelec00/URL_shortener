package com.example.URL_shortener.controller;

import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.models.AccountId;
import com.example.URL_shortener.exceptions.RegisterErrorException;
import com.example.URL_shortener.responses.RegisterResponse;
import com.example.URL_shortener.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/administration")
public class AccountController {

    AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // add acc
    @PostMapping("/register")
    public RegisterResponse registerAccount(@RequestBody AccountId accountId) {

        // Throw exc if accountId already exists
        if(accountService.findAccountById(accountId.getAccountId())){
            throw new RegisterErrorException("");
        }
        Account account = new Account();
        account.setAccountId(accountId.getAccountId());

        String password = accountService.generateRandomPassword(12);
        account.setPassword(password);

        accountService.createAccount(account);

        return new RegisterResponse(true,account.getPassword());
    }

    @ExceptionHandler
    public ResponseEntity<RegisterResponse> handleException(RegisterErrorException e) {
        RegisterResponse error = new RegisterResponse(false, "Account ID already exists!");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}

