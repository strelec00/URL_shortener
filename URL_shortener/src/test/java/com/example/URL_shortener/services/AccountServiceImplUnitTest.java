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
class AccountServiceImplUnitTest {

    @Mock
    private RestRepositoryAccount repositoryAccount;

    @InjectMocks
    private AccountServiceImpl accountService;

    Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountId("name");
        account.setPassword("password");
    }

    @Test
    void AccountServiceUnit_GeneratePassword_NotNull() {
        int length = 7;

        String pass = accountService.generateRandomPassword(length);

        assertThat(pass).isNotNull();
        assertThat(pass.length()).isEqualTo(length);
    }

    @Test
    void AccountServiceUnit_CreateAccount_EqualToSave() {
        accountService.createAccount(account);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(repositoryAccount).save(accountArgumentCaptor.capture());

        Account capturedAccount = accountArgumentCaptor.getValue();
        assertThat(capturedAccount).isEqualTo(account);
    }

    @Test
    void AccountServiceUnit_findAccountById_AccountExists_ReturnsTrue() {
        when(repositoryAccount.findById("name")).thenReturn(Optional.of(account));

        boolean accountExists = accountService.findAccountById("name");

        assertThat(accountExists).isTrue();
        verify(repositoryAccount).findById("name");
    }

    @Test
    void AccountServiceUnit_findAccountById_AccountNotExists_ReturnsFalse() {
        String accountId = "wrongName";

        when(repositoryAccount.findById(accountId)).thenReturn(Optional.empty());

        boolean accountExists = accountService.findAccountById(accountId);

        assertThat(accountExists).isFalse();
        verify(repositoryAccount).findById(accountId);
    }

    @Test
    void AccountServiceUnit_checkAuthorization_ReturnsAccount() {
        String accountId = "name";
        String password = "password";

        when(repositoryAccount.findByAccountIdAndPassword(accountId, password)).thenReturn(account);

        Account authenticatedAccount = accountService.checkAuthorization(accountId, password);

        assertThat(authenticatedAccount).isEqualTo(account);
        verify(repositoryAccount).findByAccountIdAndPassword(accountId, password);
    }


    @Test
    void AccountServiceUnit_checkAuthorization_ThrowsAuthorizationErrorException() {
        String accountId = "wrongName";
        String password = "wrongPassword";

        when(repositoryAccount.findByAccountIdAndPassword(accountId, password)).thenReturn(null);

        Account accountExists = repositoryAccount.findByAccountIdAndPassword(accountId,password);

        assertThat(accountExists).isNull();
        verify(repositoryAccount).findByAccountIdAndPassword(accountId, password);
    }


    @Test
    void AccountServiceUnit_Authenticate_ReturnsCredentials() {
        String accountId = "testName";
        String password = "testPassword";
        String authorization = "Basic " + java.util.Base64.getEncoder().encodeToString((accountId + ":" + password).getBytes());

        when(repositoryAccount.findByAccountIdAndPassword(accountId, password)).thenReturn(new Account(accountId, password));

        String[] credentials = accountService.authenticate(authorization);

        assertThat(credentials).isNotNull();
        assertThat(credentials).containsExactly(accountId, password);
    }

    @Test
    void AccountServiceUnit_Authenticate_InvalidCredentials_ThrowsAuthorizationErrorException() {
        String invalidAuthorization = "Basic " + Base64.encodeBase64String("invalidUsername:invalidPassword".getBytes());

        assertThatThrownBy(() -> accountService.authenticate(invalidAuthorization))
                .isInstanceOf(AuthorizationErrorException.class)
                .hasMessageContaining("You are not authorized to have access to URL shortening");
    }

    @Test
    void AccountServiceUnit_Authenticate_AuthNotStartWithBasic_ThrowsHeaderErrorException() {
        String invalidAuthorization = " " + Base64.encodeBase64String("invalidUsername:invalidPassword".getBytes());


        assertThatThrownBy(() -> accountService.authenticate(invalidAuthorization))
                .isInstanceOf(HeaderErrorException.class)
                .hasMessageContaining("Invalid Authorization header value");
    }

    @Test
    void AccountServiceUnit_Authenticate_MoreCredentialsThanNeeded_ThrowsHeaderErrorException() {
        String invalidAuthorization = "Basic " + Base64.encodeBase64String("invalidUsername:invalidPassword:SD".getBytes());

        assertThatThrownBy(() -> accountService.authenticate(invalidAuthorization))
                .isInstanceOf(HeaderErrorException.class)
                .hasMessageContaining("Invalid Authorization header value");
    }

    @Test
    void AccountServiceUnit_Authenticate_AccountNULL_AuthorizationErrorException() {
        String invalidAuthorization = "Basic " + Base64.encodeBase64String("invalidUsername:invalidPassword".getBytes());

        assertThatThrownBy(() -> accountService.authenticate(invalidAuthorization))
                .isInstanceOf(AuthorizationErrorException.class)
                .hasMessageContaining("");
    }
}
