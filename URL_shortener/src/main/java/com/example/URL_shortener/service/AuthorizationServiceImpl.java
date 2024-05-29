package com.example.URL_shortener.service;

import com.example.URL_shortener.model.Account;
import com.example.URL_shortener.repository.RestRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    RestRepository restRepository;

    public AuthorizationServiceImpl(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override
    public Account checkAuthorization(String accountId, String password) {
        return restRepository.findByAccountIdAndPassword(accountId,password);
    }
}
