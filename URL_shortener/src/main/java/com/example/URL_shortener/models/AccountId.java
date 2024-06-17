package com.example.URL_shortener.models;

import jakarta.validation.constraints.*;

public class AccountId {

    @NotNull(message = "AccountId cannot be null")
    @Size(min = 3, max = 15, message = "AccountId must be longer than 3 and shorter than 15")
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public AccountId(){

    }

    public AccountId(String accountId) {
        this.accountId = accountId;
    }
}
