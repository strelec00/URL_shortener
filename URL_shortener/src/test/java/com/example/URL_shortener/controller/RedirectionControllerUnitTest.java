package com.example.URL_shortener.controller;

import com.example.URL_shortener.exceptions.URLnotFoundException;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.services.URLshorteningService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class RedirectionControllerUnitTest {

    @Mock
    private URLshorteningService urlshorteningService;

    @InjectMocks
    private RedirectionController redirectionController;


    @Test
    void RedirectionControllerUnit_Redirect_redirectSuccess() {

        String hash = "kaD3";
        String generatedUrl = "http://example.com/";
        String shortUrl = generatedUrl + hash;
        String originalUrl = "http://original-url.com/fasjnk";

        URL redirectUrl = new URL();
        redirectUrl.setUrl(originalUrl);
        redirectUrl.setRedirectType(302);

        when(urlshorteningService.generateURL()).thenReturn(generatedUrl);
        when(urlshorteningService.getURLbyShortUrl(shortUrl)).thenReturn(redirectUrl);

        ResponseEntity<?> responseEntity = redirectionController.redirect(hash);

        assertNotNull(responseEntity);
        assertEquals(redirectUrl.getRedirectType(), responseEntity.getStatusCodeValue());
        assertEquals(URI.create(originalUrl), responseEntity.getHeaders().getLocation());

        verify(urlshorteningService, times(1)).generateURL();
        verify(urlshorteningService, times(1)).getURLbyShortUrl(shortUrl);
    }

    @Test
    void RedirectionControllerUnit_Redirect_redirectNotFound() {

        String hash = "nonexistentHash";

        when(urlshorteningService.generateURL()).thenReturn("http://example.com/");
        when(urlshorteningService.getURLbyShortUrl(anyString())).thenReturn(null);

        URLnotFoundException exception = assertThrows(URLnotFoundException.class,
                () -> redirectionController.redirect(hash));

        assertEquals("URL not found for hash: " + hash, exception.getMessage());

        verify(urlshorteningService, times(1)).generateURL();
        verify(urlshorteningService, times(1)).getURLbyShortUrl("http://example.com/" + hash);
    }

    @Test
    void testRedirect_InternalServerError() {
        String hash = "internalErrorHash";

        when(urlshorteningService.generateURL()).thenThrow(new RuntimeException("Internal Server Error"));

        assertThrows(RuntimeException.class, () -> redirectionController.redirect(hash));

        verify(urlshorteningService, times(1)).generateURL();
        verify(urlshorteningService, never()).getURLbyShortUrl(anyString());
    }
}
