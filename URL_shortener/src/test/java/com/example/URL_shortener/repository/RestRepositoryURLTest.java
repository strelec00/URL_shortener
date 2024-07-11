package com.example.URL_shortener.repository;

import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.models.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class RestRepositoryURLTest {

    @Autowired
    private RestRepositoryURL restRepositoryURL;

    @AfterEach
    void tearDown() {
        restRepositoryURL.deleteAll();
    }

    @Test
    void findAllByAccountId() {
        String accountId = "name";
        String url = "www.google.com";
        String shortenedUrl = "google";
        Integer redirectType = 303;

        URL urlBase = new URL(accountId, url, shortenedUrl, redirectType);
        restRepositoryURL.save(urlBase);

       List<URL> savedUrlBase =  restRepositoryURL.findAllByAccountId(accountId);

       assertThat(savedUrlBase.isEmpty()).isFalse();
       assertThat(savedUrlBase.contains(urlBase)).isTrue();
    }

    @Test
    void findByShortenedUrl() {
        String accountId = "name";
        String url = "www.google.com";
        String shortenedUrl = "google";
        Integer redirectType = 303;

        URL urlBase = new URL(accountId, url, shortenedUrl, redirectType);
        restRepositoryURL.save(urlBase);

        URL savedUrlBase =  restRepositoryURL.findByShortenedUrl(shortenedUrl);

        assertThat(savedUrlBase).isNotNull();
        assertThat(savedUrlBase).isEqualTo(urlBase);
    }
}