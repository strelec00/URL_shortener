package com.example.URL_shortener.controller;

import com.example.URL_shortener.exceptions.RegisterErrorException;
import com.example.URL_shortener.models.AccountId;
import com.example.URL_shortener.responses.RegisterResponse;
import com.example.URL_shortener.services.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerUnitTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Test
    public void AccountControllerUnitTest_Register_AccountSuccess() throws RegisterErrorException {

        String accountId = "testAccountId";
        AccountId accountIdObj = new AccountId(accountId);

        when(accountService.findAccountById(accountId)).thenReturn(false);
        when(accountService.generateRandomPassword(12)).thenReturn("generatedPas");

        RegisterResponse response = accountController.registerAccount(accountIdObj);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getPassword());
        assertEquals(12, response.getPassword().length());
    }


    @Test
    void AccountControllerUnitTest_Register_AlreadyExists() {

        String accountId = "existingAccountId";
        AccountId accountIdObj = new AccountId(accountId);

        when(accountService.findAccountById(accountId)).thenReturn(true);

        assertThrows(RegisterErrorException.class, () -> accountController.registerAccount(accountIdObj));
    }

}
