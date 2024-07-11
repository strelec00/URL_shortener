package com.example.URL_shortener.controller;

import com.example.URL_shortener.exceptions.*;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.services.URLshorteningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

public class RedirectionControllerTest {

    @Mock
    private URLshorteningService urlshorteningService;

    @InjectMocks
    private RedirectionController redirectionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(redirectionController)
                .setControllerAdvice(new GlobalUrlExceptionHandler())
                .build();
    }

    @Test
    void RedirectionController_Redirect_redirectSuccess() throws Exception {
        String hash = "fD31";
        String generatedUrl = "http://localhost.com";
        URL redirectUrl = new URL();
        redirectUrl.setUrl("https://example.com");
        redirectUrl.setRedirectType(302);

        when(urlshorteningService.generateURL()).thenReturn(generatedUrl);
        when(urlshorteningService.getURLbyShortUrl(generatedUrl + hash)).thenReturn(redirectUrl);

        mockMvc.perform(get("/" + hash)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(redirectUrl.getRedirectType()))
                .andExpect(header().string("Location", redirectUrl.getUrl()));
    }

    @Test
    void RedirectionController_Redirect_redirectNotFound() throws Exception {
        String hash = "abc123";
        String generatedUrl = "http://short.url/";

        when(urlshorteningService.generateURL()).thenReturn(generatedUrl);
        when(urlshorteningService.getURLbyShortUrl(generatedUrl + hash)).thenThrow(new URLnotFoundException("URL not found"));

        mockMvc.perform(get("/" + hash)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assertThat(result.getResolvedException()).isInstanceOf(URLnotFoundException.class)
                    .hasMessageContaining("URL not found");
                });
    }


    @Test
    void redirect_InternalServerError() throws Exception {
        // Arrange
        String hash = "abc123";
        String generatedUrl = "http://short.url/";

        when(urlshorteningService.generateURL()).thenReturn(generatedUrl);
        when(urlshorteningService.getURLbyShortUrl(generatedUrl + hash)).thenThrow(new InternalErrorException("An internal error occurred"));

        // Act & Assert
        mockMvc.perform(get("/" + hash)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
