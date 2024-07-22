package com.example.URL_shortener.repository;

import com.example.URL_shortener.models.URL;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@Transactional
class RestRepositoryURLUnitTest {

    @Autowired
    private RestRepositoryURL restRepositoryURL;

    @BeforeEach
    void setUp() {
        restRepositoryURL.deleteAll();

        // given
        URL urlBase = new URL("name","www.google.com","google", 303);
        restRepositoryURL.save(urlBase);
    }

    @AfterEach
    void tearDown() {
        restRepositoryURL.deleteAll(); // Clean up after each test
    }

    @Test
    void UrlRepositoryUnit_findAllByAccountId_returnURLs() {
       // when
       List<URL> savedUrlBase =  restRepositoryURL.findAllByAccountId("name");

       // then
       assertThat(savedUrlBase.isEmpty()).isFalse();
       assertThat(savedUrlBase.size()).isEqualTo(1);
    }

    @Test
    void UrlRepositoryUnit_findAllByAccountId_returnNull() {
       // when
       List<URL> savedUrlBase =  restRepositoryURL.findAllByAccountId("fake");

       // then
       assertThat(savedUrlBase.isEmpty());
    }

    @Test
    void UrlRepositoryUnit_findAllByShortenedId_returnURL() {
        // when
        URL savedUrlBase =  restRepositoryURL.findByShortenedUrl("google");

        // then
        assertThat(savedUrlBase).isNotNull();
        assertThat(savedUrlBase.getShortenedUrl()).isEqualTo("google");
    }

    @Test
    void UrlRepositoryUnit_findAllByShortenedId_returnNull() {
        // when
        URL savedUrlBase =  restRepositoryURL.findByShortenedUrl("fake");

        // then
        assertThat(savedUrlBase).isNull();

    }
}