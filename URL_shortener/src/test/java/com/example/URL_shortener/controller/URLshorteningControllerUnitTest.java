package com.example.URL_shortener.controller;

import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.responses.ShortUrlResponse;
import com.example.URL_shortener.services.AccountService;
import com.example.URL_shortener.services.URLshorteningService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class URLshorteningControllerUnitTest {

    @Mock
    private AccountService accountService;

    @Mock
    private URLshorteningService urlshorteningService;

    @InjectMocks
    private URLshorteningController urlshorteningController;


    @Test
    void URLshorteningControllerUnit_shortenURL_success() {

        String authorization = "Authorization";
        URLrequest urlRequest = new URLrequest();
        urlRequest.setUrl("http://example.com");
        urlRequest.setRedirectType(302);

        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        shortUrlResponse.setShortUrl("http://localhost/dsA1");

        String[] credentials = {"accountId", "password"};

        when(accountService.authenticate(authorization)).thenReturn(credentials);
        when(urlshorteningService.generateShortURL(urlRequest)).thenReturn(shortUrlResponse);

        ResponseEntity<ShortUrlResponse> responseEntity = urlshorteningController.shortenURL(authorization, urlRequest);

        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        assertEquals(shortUrlResponse, responseEntity.getBody());

        verify(accountService, times(1)).authenticate(authorization);
        verify(urlshorteningService, times(1)).generateShortURL(urlRequest);
        verify(urlshorteningService, times(1)).addURL(any(URL.class));
    }

    @Test
    void URLshorteningControllerUnit_shortenURL_unauthorized() {
        String authorization = "invalidAuthorization";
        URLrequest urlRequest = new URLrequest();

        when(accountService.authenticate(authorization)).thenThrow(new IllegalArgumentException("Invalid authorization"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> urlshorteningController.shortenURL(authorization, urlRequest));

        assertEquals("Invalid authorization", exception.getMessage());

        verify(accountService, times(1)).authenticate(authorization);
        verify(urlshorteningService, never()).generateShortURL(any(URLrequest.class));
        verify(urlshorteningService, never()).addURL(any(URL.class));
    }


    @Test
    void URLshorteningControllerUnit_shortenURL_invalidRequest() {

        String authorization = "Authorization";
        URLrequest urlRequest = new URLrequest();

        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();

        when(accountService.authenticate(authorization)).thenReturn(new String[]{"accountId", "password"});
        when(urlshorteningService.generateShortURL(urlRequest)).thenReturn(shortUrlResponse);

        ResponseEntity<ShortUrlResponse> responseEntity = urlshorteningController.shortenURL(authorization, urlRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(shortUrlResponse, responseEntity.getBody());

        verify(accountService, times(1)).authenticate(authorization);
        verify(urlshorteningService, times(1)).generateShortURL(urlRequest);
    }
}
