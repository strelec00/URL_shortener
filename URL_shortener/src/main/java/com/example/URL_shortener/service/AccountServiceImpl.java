package com.example.URL_shortener.service;

import com.example.URL_shortener.model.Account;
import com.example.URL_shortener.repository.RestRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;



@Service
public class AccountServiceImpl implements AccountService {

    RestRepository restRepository;


    public AccountServiceImpl(RestRepository restRepository) {
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


}
