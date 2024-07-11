package com.example.URL_shortener.controller;

import com.example.URL_shortener.exceptions.GlobalUrlExceptionHandler;
import com.example.URL_shortener.exceptions.RegisterErrorException;
import com.example.URL_shortener.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new GlobalUrlExceptionHandler())
                .build();
    }

    @Test
    public void AccountControllerUnitTest_Register_AccountSuccess() throws Exception {
        String accountId = "testAccountId";
        String password = "testPassword";

        when(accountService.findAccountById(accountId)).thenReturn(false);
        when(accountService.generateRandomPassword(12)).thenReturn(password);

        String requestBody = "{\"accountId\":\"" + accountId + "\"}";

        mockMvc.perform(post("/administration/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").value(password))
                .andExpect(jsonPath("$.success").value(true));
    }
    @Test
    public void AccountControllerUnitTest_Register_AlreadyExists() throws Exception {
        String accountId = "AccountId";

        // Mocking the service behavior to return true when checking for existing account
        when(accountService.findAccountById(accountId)).thenReturn(true);

        // Creating the JSON request body
        String requestBody = "{\"accountId\":\"" + accountId + "\"}";

        // Performing the POST request to the endpoint and asserting expectations
        mockMvc.perform(post("/administration/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest()) // Expecting HTTP 400
                .andExpect(result -> {
                    // Additional assertions on the result
                    assertThat(result.getResolvedException()).isInstanceOf(RegisterErrorException.class);
                    assertThat(result.getResolvedException().getMessage()).contains("Account ID already exists!");
                });
    }


    @Test
    public void AccountControllerUnitTest_Register_InvalidRequest() throws Exception {
        String requestBody = "{\"invalidField\":\"invalidValue\"}";

        mockMvc.perform(post("/administration/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assert result.getResolvedException() instanceof MethodArgumentNotValidException;
                });
    }
}
