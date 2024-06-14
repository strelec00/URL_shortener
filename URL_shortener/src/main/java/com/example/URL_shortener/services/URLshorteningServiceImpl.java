package com.example.URL_shortener.services;
import com.example.URL_shortener.exceptions.RedirectTypeException;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.repository.RestRepositoryURL;
import com.example.URL_shortener.responses.ShortUrlResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

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
    public ShortUrlResponse generateURL(URLrequest url) {

        ShortUrlResponse response = new ShortUrlResponse();
        String hash = generateRandomHash(4);


        String shortUrl =  scheme + "://" + serverAddress + ":" + port + contextPath + "/" + hash;

        response.setShortUrl(shortUrl);

        return response;
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


}
