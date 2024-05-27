package com.example.URL_shortener.service;

import com.example.URL_shortener.model.Account;


public interface AccountService {
    void createAccount(Account account);

    String generateRandomPassword(int length);

    Boolean findAccountById(String accountId);
}
