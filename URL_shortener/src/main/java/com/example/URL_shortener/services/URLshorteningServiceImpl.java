package com.example.URL_shortener.services;
import com.example.URL_shortener.exceptions.RedirectTypeException;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.repository.RestRepositoryURL;
import com.example.URL_shortener.responses.ShortUrlResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class URLshorteningServiceImpl implements URLshorteningService {

    private final RestRepositoryURL repositoryURL;

    @Value("${server.address:localhost}")
    private String serverAddress;

    @Value("${server.port:8080}")
    private Integer port;

    @Value("${server.scheme:http}")
    private String scheme;

    @Value("${server.servlet.context-path:}")
    private String contextPath;


    public URLshorteningServiceImpl(RestRepositoryURL repositoryURL) {
        this.repositoryURL = repositoryURL;
    }

    @Override
    public String generateRandomHash(int length)  {
        return RandomStringUtils.random(length, true, true);
    }

    @Override
    public ShortUrlResponse generateShortURL(URLrequest url) {

        ShortUrlResponse response = new ShortUrlResponse();
        String hash = generateRandomHash(4);


        String shortUrl =  generateURL() + hash;

        response.setShortUrl(shortUrl);

        return response;
    }

    @Override
    public String generateURL() {
        return scheme + "://" + serverAddress + ":" + port + contextPath + "/";
    }

    public void addURL(URL url) {
        repositoryURL.save(url);
    }

    @Override
    public void getRedirectType(Integer redirectType, URLrequest url) {
        if (redirectType == null) {
            url.setRedirectType(302);
        } else if (url.getRedirectType() != 302 && url.getRedirectType() != 301) {
            throw new RedirectTypeException("RedirectType can ONLY be 301 | 302");
        } else {
            url.setRedirectType(url.getRedirectType());
        }

    }

    @Override
    public Map<String, Integer> findAllByAccountId(String accountId) {
        List<URL> urls = repositoryURL.findAllByAccountId(accountId);
        Map<String, Integer> map = new HashMap<>();
        Integer count = 0;
        for (URL url : urls) {
            String urlString = url.getUrl();
            map.put(urlString, map.getOrDefault(urlString, 0) + 1);
        }
        return map;
    }

    @Override
    public URL getURLbyShortUrl(String shortUrl) {
        return repositoryURL.findByShortenedUrl(shortUrl);
    }


}
