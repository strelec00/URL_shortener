package com.example.URL_shortener.services;

import com.example.URL_shortener.exceptions.AuthorizationErrorException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.URL_shortener.exceptions.HeaderErrorException;
import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.repository.RestRepositoryAccount;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private RestRepositoryAccount repositoryAccount;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(repositoryAccount);
    }

    @Test
    void AccountService_GeneratePassword_NotNull() {
        int length = 7;

        String pass = accountService.generateRandomPassword(length);

        assertThat(pass).isNotNull();
        assertThat(pass.length()).isEqualTo(length);
    }

    @Test
    void AccountService_CreateAccount_EqualToSave() {
        String accountId = "testUser";
        String password = "testPassword";
        Account account = new Account(accountId, password);

        accountService.createAccount(account);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(repositoryAccount).save(accountArgumentCaptor.capture());

        Account capturedAccount = accountArgumentCaptor.getValue();
        assertThat(capturedAccount).isEqualTo(account);
    }

    @Test
    void AccountService_findAccountById_AccountExists_ReturnsTrue() {
        String accountId = "testUser";
        String password = "testPassword";
        Account account = new Account(accountId, password);

        when(repositoryAccount.findById(accountId)).thenReturn(Optional.of(account));

        boolean accountExists = accountService.findAccountById(accountId);

        assertThat(accountExists).isTrue();
        verify(repositoryAccount).findById(accountId);
    }

    @Test
    void AccountService_findAccountById_AccountNotExists_ReturnsFalse() {
        String accountId = "nonExistingUser";

        when(repositoryAccount.findById(accountId)).thenReturn(Optional.empty());

        boolean accountExists = accountService.findAccountById(accountId);

        assertThat(accountExists).isFalse();
        verify(repositoryAccount).findById(accountId);
    }

    @Test
    void AccountService_checkAuthorization_ReturnsAccount() {
        String accountId = "testUser";
        String password = "testPassword";
        Account account = new Account(accountId, password);

        when(repositoryAccount.findByAccountIdAndPassword(accountId, password)).thenReturn(account);

        Account authenticatedAccount = accountService.checkAuthorization(accountId, password);

        assertThat(authenticatedAccount).isEqualTo(account);
        verify(repositoryAccount).findByAccountIdAndPassword(accountId, password);
    }


    @Test
    void AccountService_checkAuthorization_ThrowsAuthorizationErrorException() {
        String accountId = "testUser";
        String password = "wrongPassword";

        when(repositoryAccount.findByAccountIdAndPassword(accountId, password)).thenReturn(null);

        Account accountExists = repositoryAccount.findByAccountIdAndPassword(accountId,password);

        assertThat(accountExists).isNull();
        verify(repositoryAccount).findByAccountIdAndPassword(accountId, password);
    }


    @Test
    void AccountService_Authenticate_ReturnsCredentials() {
        String accountId = "testUser";
        String password = "testPassword";
        String authorization = "Basic " + java.util.Base64.getEncoder().encodeToString((accountId + ":" + password).getBytes());

        when(repositoryAccount.findByAccountIdAndPassword(accountId, password)).thenReturn(new Account(accountId, password));

        String[] credentials = accountService.authenticate(authorization);

        assertThat(credentials).isNotNull();
        assertThat(credentials).containsExactly(accountId, password);
    }

    @Test
    void AccountService_Authenticate_InvalidCredentials_ThrowsAuthorizationErrorException() {
        String invalidAuthorization = "Basic " + Base64.encodeBase64String("invalidUsername:invalidPassword".getBytes());

        assertThatThrownBy(() -> accountService.authenticate(invalidAuthorization))
                .isInstanceOf(AuthorizationErrorException.class)
                .hasMessageContaining("You are not authorized to have access to URL shortening");
    }

    @Test
    void AccountService_Authenticate_AuthNotStartWithBasic_ThrowsHeaderErrorException() {
        String invalidAuthorization = " " + Base64.encodeBase64String("invalidUsername:invalidPassword".getBytes());


        assertThatThrownBy(() -> accountService.authenticate(invalidAuthorization))
                .isInstanceOf(HeaderErrorException.class)
                .hasMessageContaining("Invalid Authorization header value");
    }

    @Test
    void AccountService_Authenticate_MoreCredentialsThanNeeded_ThrowsHeaderErrorException() {
        String invalidAuthorization = "Basic " + Base64.encodeBase64String("invalidUsername:invalidPassword:SD".getBytes());

        assertThatThrownBy(() -> accountService.authenticate(invalidAuthorization))
                .isInstanceOf(HeaderErrorException.class)
                .hasMessageContaining("Invalid Authorization header value");
    }

    @Test
    void AccountService_Authenticate_AccountNULL_AuthorizationErrorException() {
        String invalidAuthorization = "Basic " + Base64.encodeBase64String("invalidUsername:invalidPassword".getBytes());

        assertThatThrownBy(() -> accountService.authenticate(invalidAuthorization))
                .isInstanceOf(AuthorizationErrorException.class)
                .hasMessageContaining("You are not authorized to have access to URL shortening");
    }
}
