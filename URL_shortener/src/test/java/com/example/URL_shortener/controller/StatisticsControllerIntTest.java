package com.example.URL_shortener.controller;

import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.services.AccountService;
import com.example.URL_shortener.services.URLshorteningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StatisticsControllerIntTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private URLshorteningService urlshorteningService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void StatisticControllerInt_GetStatistics_Success() throws Exception {
        String accountId = "name";
        String password = "password";

        Account account = new Account(accountId, password);
        accountService.createAccount(account);

        URL url1 = new URL(accountId, "http://example1.com", "short1", 301);
        URL url3 = new URL(accountId, "http://example1.com", "short3", 302);
        URL url2 = new URL(accountId, "http://example2.com", "short2", 301);
        urlshorteningService.addURL(url1);
        urlshorteningService.addURL(url2);
        urlshorteningService.addURL(url3);

        String authorizationHeader = "Basic " + Base64.getEncoder().encodeToString((accountId + ":" + password).getBytes());

        mockMvc.perform(get("/administration/statistics")
                        .header("Authorization", authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"http://example1.com\":2,\"http://example2.com\":1}"));
    }


    @Test
    void StatisticControllerInt_GetStatistics_AuthenticationFailed() throws Exception {

        String authorizationHeader = "Basic " + Base64.getEncoder().encodeToString(("fakeName" + ":" + "fakePass").getBytes());

        mockMvc.perform(get("/administration/statistics")
                        .header("Authorization", authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}