package com.example.URL_shortener.controller;

import com.example.URL_shortener.exceptions.GlobalUrlExceptionHandler;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.responses.ShortUrlResponse;
import com.example.URL_shortener.services.AccountService;
import com.example.URL_shortener.services.URLshorteningService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class URLshorteningControllerTest {

    @Mock
    private URLshorteningService urlshorteningService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private URLshorteningController urlshorteningController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(urlshorteningController)
                .setControllerAdvice(new GlobalUrlExceptionHandler())
                .build();
        objectMapper = new ObjectMapper(); // Initialize the objectMapper
    }

    @Test
    void URLshorteningController_shortenURL_success() throws Exception {
        URLrequest urlRequest = new URLrequest();
        urlRequest.setUrl("http://example.com");
        urlRequest.setRedirectType(301);

        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        shortUrlResponse.setShortUrl("http://short.url/abc123");

        when(urlshorteningService.generateShortURL(any(URLrequest.class))).thenReturn(shortUrlResponse);
        when(accountService.authenticate(anyString())).thenReturn(new String[]{"accountId"});

        String jsonRequest = objectMapper.writeValueAsString(urlRequest);

        mockMvc.perform(post("/administration/short")
                        .header("Authorization", "Basic Auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().is(301))
                .andExpect(jsonPath("$.shortUrl").value("http://short.url/abc123"));

        verify(urlshorteningService, times(1)).generateShortURL(any(URLrequest.class));
        verify(accountService, times(1)).authenticate(anyString());
    }


    @Test
    void URLshorteningController_shortenURL_unauthorized() throws Exception {
        URLrequest urlRequest = new URLrequest();
        urlRequest.setUrl("http://example.com");
        urlRequest.setRedirectType(301);

        String jsonRequest = objectMapper.writeValueAsString(urlRequest);

        mockMvc.perform(post("/administration/short")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void URLshorteningController_shortenURL_invalidRequest() throws Exception {
        URLrequest urlRequest = new URLrequest();

        String jsonRequest = objectMapper.writeValueAsString(urlRequest);

        mockMvc.perform(post("/administration/short")
                        .header("Authorization", "Basic Auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void URLshorteningController_shortenURL_internalServerError() throws Exception {
        URLrequest urlRequest = new URLrequest();
        urlRequest.setUrl("http://example.com");
        urlRequest.setRedirectType(301);

        String jsonRequest = objectMapper.writeValueAsString(urlRequest);

        // Mock the service to throw an exception
        when(urlshorteningService.generateShortURL(any(URLrequest.class)))
                .thenThrow(new RuntimeException(""));

        mockMvc.perform(MockMvcRequestBuilders.post("/administration/short")
                        .header("Authorization", "Basic Auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }


}
