package com.example.URL_shortener.repository;

import com.example.URL_shortener.models.URL;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class RestRepositoryURLIntTest {

    @Autowired
    private RestRepositoryURL repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void UrlRepositoryInt_findAllByAccountId_returnURLs() {
        // given
        String accountId = "name";

        URL url1 = new URL();
        url1.setAccountId(accountId);
        url1.setUrl("http://google.com");
        url1.setRedirectType(302);
        repository.save(url1);

        URL url2 = new URL();
        url2.setAccountId(accountId);
        url2.setUrl("http://google.org");
        url2.setRedirectType(301);
        repository.save(url2);

        // when
        List<URL> foundURLs = repository.findAllByAccountId(accountId);

        // then
        assertThat(foundURLs).isNotEmpty();
        assertThat(foundURLs.size()).isEqualTo(2);
    }

    @Test
    void UrlRepositoryInt_findAllByAccountId_returnNull() {
        // when
        List<URL> foundURLs = repository.findAllByAccountId("name");

        // then
        assertThat(foundURLs).isEmpty();
    }

    @Test
    void UrlRepositoryInt_findAllByShortenedId_returnURL() {
        // given
        URL url = new URL();
        url.setShortenedUrl("google");
        url.setUrl("http://google.com");
        url.setRedirectType(301);
        repository.save(url);

        // when
        URL foundURL = repository.findByShortenedUrl("google");

        // then
        assertThat(foundURL).isNotNull();
        assertThat(foundURL.getShortenedUrl()).isEqualTo("google");
    }
    @Test
    void UrlRepositoryInt_findAllByShortenedId_returnNull() {
        // when
        URL foundURL = repository.findByShortenedUrl("google");

        // then
        assertThat(foundURL).isNull();
    }
}
