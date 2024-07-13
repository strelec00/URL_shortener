package com.example.URL_shortener.controller;

import com.example.URL_shortener.services.AccountService;
import com.example.URL_shortener.services.URLshorteningService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerUnitTest {

    @Mock
    private AccountService accountService;

    @Mock
    private URLshorteningService urlshorteningService;

    @InjectMocks
    private StatisticsController statisticsController;


    @Test
    void StatisticControllerUnit_GetStatistics_Success() {

        String authorization = "Authorization";
        String accountId = "accountId";
        String[] credentials = {accountId, "password"};

        Map<String, Integer> expectedStatistics = new HashMap<>();
        expectedStatistics.put("shortenedUrls", 10);

        when(accountService.authenticate(authorization)).thenReturn(credentials);
        when(urlshorteningService.findAllByAccountId(accountId)).thenReturn(expectedStatistics);

        Map<String, Integer> actualStatistics = statisticsController.GetStatistics(authorization);

        assertEquals(expectedStatistics, actualStatistics);

        verify(accountService, times(1)).authenticate(authorization);
        verify(urlshorteningService, times(1)).findAllByAccountId(accountId);
    }

    @Test
    void StatisticControllerUnit_GetStatistics_AuthenticationFailed() {

        String authorization = "invalidAuthorization";

        when(accountService.authenticate(authorization)).thenThrow(new IllegalArgumentException("Invalid authorization"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> statisticsController.GetStatistics(authorization));

        assertEquals("Invalid authorization", exception.getMessage());

        verify(accountService, times(1)).authenticate(authorization);
        verify(urlshorteningService, never()).findAllByAccountId(anyString());
    }
}
