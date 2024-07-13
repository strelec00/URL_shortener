package com.example.URL_shortener.controller;

import com.example.URL_shortener.exceptions.RegisterErrorException;
import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerIntTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void AccountControllerIntTest_Register_AccountSuccess() throws Exception {
        String accountId = "testAccountId";

        String requestBody = "{\"accountId\":\"" + accountId + "\"}";

        mockMvc.perform(post("/administration/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }
    @Test
    public void AccountControllerIntTest_Register_AlreadyExists() throws Exception {
        String accountId = "name";

        Account existingAccount = new Account(accountId, "Password");
        accountService.createAccount(existingAccount);

        String requestBody = "{\"accountId\":\"" + accountId + "\"}";

        mockMvc.perform(post("/administration/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertThat(result.getResolvedException()).isInstanceOf(RegisterErrorException.class);
                    assertThat(result.getResolvedException().getMessage()).contains("Account ID already exists!");
                });
    }


    @Test
    public void AccountControllerTest_Register_InvalidRequest() throws Exception {
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