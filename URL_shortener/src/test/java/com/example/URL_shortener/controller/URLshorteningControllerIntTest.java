package com.example.URL_shortener.controller;

import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.services.AccountService;
import com.example.URL_shortener.services.URLshorteningService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class URLshorteningControllerIntTest {

    @Autowired
    private URLshorteningService urlshorteningService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper(); // Initialize the objectMapper
    }

    @Test
    void URLshorteningControllerInt_shortenURL_success() throws Exception {
        URLrequest urlRequest = new URLrequest();
        urlRequest.setUrl("http://example.com");
        urlRequest.setRedirectType(301);

        String jsonRequest = objectMapper.writeValueAsString(urlRequest);

        Account account = new Account("name", "password");
        accountService.createAccount(account);

        String authorizationHeader = "Basic " + Base64.getEncoder().encodeToString(("name" + ":" + "password").getBytes());

        mockMvc.perform(post("/administration/short")
                        .header("Authorization", authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().is(urlRequest.getRedirectType()))
                .andExpect(jsonPath("$.shortUrl").isNotEmpty());

    }


    @Test
    void URLshorteningControllerInt_shortenURL_unauthorized() throws Exception {
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
    void URLshorteningControllerInt_shortenURL_invalidRequest() throws Exception {
        URLrequest urlRequest = new URLrequest();

        String jsonRequest = objectMapper.writeValueAsString(urlRequest);

        mockMvc.perform(post("/administration/short")
                        .header("Authorization", "Basic Auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }



}