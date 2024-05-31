package com.example.URL_shortener.services;

import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.repository.RestRepositoryAccount;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;



@Service
public class AccountServiceImpl implements AccountService {

    RestRepositoryAccount restRepository;


    public AccountServiceImpl(RestRepositoryAccount restRepository) {
        this.restRepository = restRepository;
    }


    //password generator
    public String generateRandomPassword(int length) {
        return RandomStringUtils.random(length, true, true);
    }


    @Override
    @Transactional
    public void createAccount(Account account) {
        restRepository.save(account);
    }

    @Override
    public Boolean findAccountById(String accountId) {
        return restRepository.findById(accountId).isPresent();
    }

    @Override
    public Account checkAuthorization(String accountId, String password) {
        return restRepository.findByAccountIdAndPassword(accountId,password);
    }

}
