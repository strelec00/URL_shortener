package com.example.URL_shortener.services;

import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.responses.ShortUrlResponse;

import java.util.Map;

public interface URLshorteningService{
    String generateRandomHash(int length);

    ShortUrlResponse generateURL(URLrequest url);

    void addURL(URL url);

    void getRedirectType(Integer redirectType, URLrequest url);

    Map<String, Integer> findAllByAccountId(String accountId);

}
