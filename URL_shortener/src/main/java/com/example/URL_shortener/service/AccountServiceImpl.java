package com.example.URL_shortener.service;

import com.example.URL_shortener.model.Account;
import com.example.URL_shortener.repository.RestRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    RestRepository restRepository;

    public AccountServiceImpl(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override
    @Transactional
    public void createAccount(Account account) {
        restRepository.save(account);
    }
}
