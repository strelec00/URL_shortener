package com.example.URL_shortener.services;

import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.repository.RestRepositoryURL;
import com.example.URL_shortener.responses.ShortUrlResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class URLshorteningServiceImpl implements URLshorteningService {

    RestRepositoryURL repositoryURL;

    public URLshorteningServiceImpl(RestRepositoryURL repositoryURL) {
        this.repositoryURL = repositoryURL;
    }

    @Override
    public String generateRandomHash(int length)  {
        return RandomStringUtils.random(length, true, true);
    }

    @Override
    public ShortUrlResponse generateURL(URLrequest url) {
        ShortUrlResponse response = new ShortUrlResponse();
        String hash = generateRandomHash(4);
        String shortUrl = "http://localhost:8080/" + hash;

        response.setShortUrl(shortUrl);

        return response;
    }

    public void addURL(URL url) {
        repositoryURL.save(url);
    }

}
