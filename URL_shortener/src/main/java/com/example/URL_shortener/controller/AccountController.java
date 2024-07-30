package com.example.URL_shortener.controller;

import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.models.AccountId;
import com.example.URL_shortener.exceptions.RegisterErrorException;
import com.example.URL_shortener.responses.RegisterResponse;
import com.example.URL_shortener.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administration")
public class AccountController {

    AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(description = "Creates a new account using the accountId provided in the request body. If register succeeded, response object will contain success: true and auto-generated password for created account. If register didn't succeed, response will contain success: false and description on why it didn't work.",
            security = @SecurityRequirement(name = "basicAuth"))
    @Tag(name = "Account register", description = "POST method for registering an Account.")
    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public RegisterResponse registerAccount(@Parameter(description = "AccountId of wanted account name: ") @Valid @RequestBody AccountId accountId) {

        // Throw exc if accountId already exists
        if (accountService.findAccountById(accountId.getAccountId())) {
            throw new RegisterErrorException("Account ID already exists!");
        }
        Account account = new Account();
        account.setAccountId(accountId.getAccountId());

        String password = accountService.generateRandomPassword(12);
        account.setPassword(password);

        accountService.createAccount(account);

        return new RegisterResponse(account.getPassword(), true);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public boolean loginRegister(@RequestBody Account account) {
        Account accountExist = accountService.checkAuthorization(account.getAccountId(), account.getPassword());

        if (accountExist == null) {
            throw new RegisterErrorException("Account does not exist!");
        }
        return true;
    }
}
