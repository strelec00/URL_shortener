package com.example.URL_shortener.services;

import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.responses.ShortUrlResponse;

public interface URLshorteningService{
    String generateRandomHash(int length);

    ShortUrlResponse generateURL(URLrequest url);

    public void addURL(URL url);
}
