package com.example.URL_shortener.service;

import com.example.URL_shortener.model.Account;
import org.springframework.stereotype.Service;

public interface AuthorizationService{
    Account checkAuthorization(String accountId, String password);
}
