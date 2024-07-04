package com.example.URL_shortener.services;

import com.example.URL_shortener.exceptions.AuthorizationErrorException;
import com.example.URL_shortener.exceptions.HeaderErrorException;
import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.repository.RestRepositoryAccount;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
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


    @Override
    public String[] authenticate(String authorization) {

        String encodedAuthorization = authorization.substring(6).trim();
        String decodedAuthorization = new String(Base64.decodeBase64(encodedAuthorization));
        String[] credentials = decodedAuthorization.split(":");

        // error handling - password empty
        if (credentials.length != 2 || !authorization.startsWith("Basic ")) {
            throw new HeaderErrorException("Invalid Authorization header value");
        }

        // error handling - provjera autorizacije
        Account account = checkAuthorization(credentials[0], credentials[1]);
        if (account == null) {
            throw new AuthorizationErrorException("You are not authorized to have access to URL shortening");
        }

        return credentials;

    }

}
