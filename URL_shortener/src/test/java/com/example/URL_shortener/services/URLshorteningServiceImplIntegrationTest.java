package com.example.URL_shortener.services;

import com.example.URL_shortener.exceptions.RedirectTypeException;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.repository.RestRepositoryURL;
import com.example.URL_shortener.responses.ShortUrlResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class URLshorteningServiceImplIntegrationTest {

    @Autowired
    private URLshorteningServiceImpl urlshorteningService;

    @Autowired
    private RestRepositoryURL restRepositoryURL;

    @BeforeEach
    void setUp() {
        restRepositoryURL.deleteAll();
    }

    @Test
    void ShorteningServiceInt_GenerateRandomHash_IsNotNULL() {
        int length = 7;

        String hash = urlshorteningService.generateRandomHash(length);

        assertThat(hash).isNotNull();
        assertThat(hash.length()).isEqualTo(length);
    }

    @Test
    void ShorteningServiceInt_GenerateShortURL_IsNotNULL() {
        String url = "http://www.example.com";
        Integer redirectType = 302;

        URLrequest urlRequest = new URLrequest(url, redirectType);

        ShortUrlResponse response = urlshorteningService.generateShortURL(urlRequest);

        assertThat(response).isNotNull();
        assertThat(response.getShortUrl()).isNotNull();
        assertThat(response.getShortUrl().contains("://")).isTrue();
    }

    @Test
    void ShorteningServiceInt_GenerateURL_IsNotNULL() {
        String generatedUrl = urlshorteningService.generateURL();

        assertThat(generatedUrl).isNotNull();
        assertThat(generatedUrl.contains("://")).isTrue();
    }

    @Test
    void ShorteningServiceInt_AddURL_returnURL() {
        String accountId = "test_account";
        String url = "http://www.example.com";
        String shortenedUrl = "abcd";
        Integer redirectType = 301;

        URL urlObject = new URL(accountId, url, shortenedUrl, redirectType);

        urlshorteningService.addURL(urlObject);

        URL retrievedURL = restRepositoryURL.findByShortenedUrl(shortenedUrl);

        assertThat(retrievedURL).isNotNull();
        assertThat(retrievedURL.getUrl()).isEqualTo(urlObject.getUrl());
        assertThat(retrievedURL.getShortenedUrl()).isEqualTo(urlObject.getShortenedUrl());
        assertThat(retrievedURL.getRedirectType()).isEqualTo(urlObject.getRedirectType());
        assertThat(retrievedURL.getAccountId()).isEqualTo(urlObject.getAccountId());
    }

    @Test
    void ShorteningServiceInt_GetRedirectType_ValidRedirectType() {
        URLrequest urlRequest = new URLrequest("http://www.example.com", 301);

        urlshorteningService.getRedirectType(urlRequest.getRedirectType(), urlRequest);

        assertThat(urlRequest.getRedirectType()).isEqualTo(301);
    }

    @Test
    void ShorteningServiceInt_GetRedirectType_NullRedirectType() {
        URLrequest urlRequest = new URLrequest("http://www.example.com", null);

        urlshorteningService.getRedirectType(urlRequest.getRedirectType(), urlRequest);

        assertThat(urlRequest.getRedirectType()).isEqualTo(302);
    }

    @Test
    void ShorteningServiceInt_GetRedirectType_InvalidRedirectType() {
        URLrequest urlRequest = new URLrequest("http://www.example.com", 303);

        assertThatThrownBy(() -> urlshorteningService.getRedirectType(urlRequest.getRedirectType(), urlRequest))
                .isInstanceOf(RedirectTypeException.class)
                .hasMessageContaining("RedirectType can ONLY be 301 | 302");
    }

    @Test
    void ShorteningServiceInt_FindAllByAccountId_ValidAccountId() {
        String accountId = "test_account";
        URL url1 = new URL(accountId, "http://www.example.com", "abcd", 301);
        URL url2 = new URL(accountId, "http://www.google.com", "efgh", 302);
        URL url3 = new URL(accountId, "http://www.example.com", "fsea", 301);
        List<URL> urls = Arrays.asList(url1, url2, url3);

        restRepositoryURL.saveAll(urls);

        Map<String, Integer> urlCounts = urlshorteningService.findAllByAccountId(accountId);

        assertThat(urlCounts).isNotEmpty();
        assertThat(urlCounts.get("http://www.example.com")).isEqualTo(2);
        assertThat(urlCounts.get("http://www.google.com")).isEqualTo(1);
    }

    @Test
    void ShorteningServiceInt_FindAllByAccountId_InvalidAccountId() {
        String accountId = "invalid_account";

        Map<String, Integer> urlCounts = urlshorteningService.findAllByAccountId(accountId);

        assertThat(urlCounts).isEmpty();
    }

    @Test
    void ShorteningServiceInt_GetURLbyShortUrl_ValidShortUrl() {
        String shortUrl = "abcd";
        URL url = new URL("test_account", "http://www.example.com", shortUrl, 301);

        restRepositoryURL.save(url);

        URL retrievedUrl = urlshorteningService.getURLbyShortUrl(shortUrl);

        assertThat(retrievedUrl).isNotNull();
        assertThat(retrievedUrl.getUrl()).isEqualTo(url.getUrl());
        assertThat(retrievedUrl.getShortenedUrl()).isEqualTo(url.getShortenedUrl());
        assertThat(retrievedUrl.getAccountId()).isEqualTo(url.getAccountId());
        assertThat(retrievedUrl.getRedirectType()).isEqualTo(url.getRedirectType());
    }

    @Test
    void ShorteningServiceInt_GetURLbyShortUrl_InvalidShortUrl() {
        String shortUrl = "wrongShortUrl";

        URL retrievedUrl = urlshorteningService.getURLbyShortUrl(shortUrl);

        assertThat(retrievedUrl).isNull();
    }
}
