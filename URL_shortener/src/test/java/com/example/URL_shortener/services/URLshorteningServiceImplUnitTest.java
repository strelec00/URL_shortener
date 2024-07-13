package com.example.URL_shortener.services;

import com.example.URL_shortener.exceptions.RedirectTypeException;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.repository.RestRepositoryURL;
import com.example.URL_shortener.responses.ShortUrlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class URLshorteningServiceImplUnitTest {

    @Mock
    private RestRepositoryURL restRepositoryURL;

    @InjectMocks
    private URLshorteningServiceImpl urlshorteningService;

    @BeforeEach
    void setUp() {
        urlshorteningService = new URLshorteningServiceImpl(restRepositoryURL);
    }

    @Test
    void ShorteningServiceUnit_GenerateRandomHash_IsNotNULL() {
        int length = 7;

        String hash = urlshorteningService.generateRandomHash(length);

        assertThat(hash).isNotNull();
        assertThat(hash.length()).isEqualTo(length);
    }

    @Test
    void ShorteningServiceUnit_GenerateShortURL_IsNotNULL() {
        String url = "www.google.com";
        Integer redirectType = 302;

        URLrequest urlRequest = new URLrequest(url, redirectType);

        ShortUrlResponse response = urlshorteningService.generateShortURL(urlRequest);

        assertThat(response).isNotNull();
        assertThat(response.getShortUrl()).isNotNull();
        assertThat(response.getShortUrl().contains("://")).isTrue();
    }

    @Test
    void ShorteningServiceUnit_GenerateURL_IsNotNULL() {
        String generatedUrl = urlshorteningService.generateURL();

        assertThat(generatedUrl).isNotNull();
        assertThat(generatedUrl.contains("://")).isTrue();
    }

    @Test
    void ShorteningServiceUnit_AddURL_returnURL() {
        String accountId = "test_account";
        String url = "www.example.com/trail/bake/31309";
        String shortenedUrl = "abcd";
        Integer redirectType = 301;

        URL urlObject = new URL(accountId, url, shortenedUrl, redirectType);

        urlshorteningService.addURL(urlObject);

        ArgumentCaptor<URL> urlArgumentCaptor = ArgumentCaptor.forClass(URL.class);
        verify(restRepositoryURL).save(urlArgumentCaptor.capture());

        URL capturedURL = urlArgumentCaptor.getValue();
        assertThat(capturedURL).isEqualTo(urlObject);
    }

    @Test
    void ShorteningServiceUnit_GetRedirectType_ValidRedirectType() {
        URLrequest urlRequest = new URLrequest("www.example.com", 301);

        urlshorteningService.getRedirectType(urlRequest.getRedirectType(), urlRequest);

        assertThat(urlRequest.getRedirectType()).isEqualTo(301);
    }

    @Test
    void ShorteningServiceUnit_GetRedirectType_NullRedirectType() {
        URLrequest urlRequest = new URLrequest("www.example.com", null);

        urlshorteningService.getRedirectType(urlRequest.getRedirectType(), urlRequest);

        assertThat(urlRequest.getRedirectType()).isEqualTo(302);
    }

    @Test
    void ShorteningServiceUnit_GetRedirectType_InvalidRedirectType() {
        URLrequest urlRequest = new URLrequest("www.example.com", 303);

        assertThatThrownBy(() -> urlshorteningService.getRedirectType(urlRequest.getRedirectType(), urlRequest))
                .isInstanceOf(RedirectTypeException.class)
                .hasMessageContaining("RedirectType can ONLY be 301 | 302");
    }

    @Test
    void ShorteningServiceUnit_FindAllByAccountId_ValidAccountId() {
        String accountId = "test_account";
        URL url1 = new URL(accountId, "www.example.com", "abcd", 301);
        URL url3 = new URL(accountId, "www.example.com", "lmkr", 301);
        URL url2 = new URL(accountId, "www.google.com", "efgh", 302);
        List<URL> urls = Arrays.asList(url1, url2, url3);

        when(restRepositoryURL.findAllByAccountId(accountId)).thenReturn(urls);

        Map<String, Integer> urlCounts = urlshorteningService.findAllByAccountId(accountId);

        assertThat(urlCounts).isNotEmpty();
        assertThat(urlCounts.get("www.example.com")).isEqualTo(2);
        assertThat(urlCounts.get("www.google.com")).isEqualTo(1);
    }

    @Test
    void ShorteningServiceUnit_FindAllByAccountId_InvalidAccountId() {
        String accountId = "test_account";

        when(restRepositoryURL.findAllByAccountId(accountId)).thenReturn(Collections.emptyList());

        Map<String, Integer> urlCounts = urlshorteningService.findAllByAccountId(accountId);

        assertThat(urlCounts).isEmpty();
    }

    @Test
    void ShorteningServiceUnit_GetURLbyShortUrl_ValidShortUrl() {
        String shortUrl = "abcd";
        URL url = new URL("test_account", "www.example.com", shortUrl, 301);

        when(restRepositoryURL.findByShortenedUrl(shortUrl)).thenReturn(url);

        URL retrievedUrl = urlshorteningService.getURLbyShortUrl(shortUrl);

        assertThat(retrievedUrl).isNotNull();
        assertThat(retrievedUrl).isEqualTo(url);
    }

    @Test
    void ShorteningServiceUnit_GetURLbyShortUrl_InvalidShortUrl() {
        String shortUrl = "wrongShortUrl";

        when(restRepositoryURL.findByShortenedUrl(shortUrl)).thenReturn(null);

        URL retrievedUrl = urlshorteningService.getURLbyShortUrl(shortUrl);

        assertThat(retrievedUrl).isNull();
    }

}
