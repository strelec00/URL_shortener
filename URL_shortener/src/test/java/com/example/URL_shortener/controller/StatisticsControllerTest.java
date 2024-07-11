package com.example.URL_shortener.controller;

import com.example.URL_shortener.exceptions.AuthorizationErrorException;
import com.example.URL_shortener.exceptions.GlobalUrlExceptionHandler;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.services.AccountService;
import com.example.URL_shortener.services.URLshorteningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class StatisticsControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private URLshorteningService urlshorteningService;

    @InjectMocks
    private StatisticsController statisticsController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(statisticsController)
                .setControllerAdvice(new GlobalUrlExceptionHandler())
                .build();
    }

    @Test
    void StatisticController_GetStatistics_Success() throws Exception {
        String[] credentials = {"accountId"};
        Map<String, Integer> t = new HashMap<>();
        t.put("url1", 21);
        t.put("url2", 2);

        when(accountService.authenticate(anyString())).thenReturn(credentials);
        when(urlshorteningService.findAllByAccountId(anyString())).thenReturn(t);

        mockMvc.perform(get("/administration/statistics")
                .header("Authorization", "Basic Auth")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"url1\":21,\"url2\":2}"));

    }


    @Test
    void StatisticController_GetStatistics_AuthenticationFailed() throws Exception {
        when(accountService.authenticate(anyString())).thenThrow(new AuthorizationErrorException("You are not authorized to have access to URL shortening"));

        mockMvc.perform(get("/administration/statistics")
                        .header("Authorization", "Basic Auth")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void StatisticController_GetStatistics_internalServerError() throws Exception {

        when(urlshorteningService.findAllByAccountId(anyString())).thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(get("/administration/statistics")
                        .header("Authorization", "Basic Auth")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

}