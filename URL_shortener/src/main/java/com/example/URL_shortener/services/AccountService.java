package com.example.URL_shortener.services;

import com.example.URL_shortener.models.Account;


public interface AccountService {
    void createAccount(Account account);

    String generateRandomPassword(int length);

    Boolean findAccountById(String accountId);

    Account checkAuthorization(String accountId, String password);

}
