package com.example.URL_shortener.services;

import com.example.URL_shortener.exceptions.AuthorizationErrorException;
import com.example.URL_shortener.exceptions.HeaderErrorException;
import com.example.URL_shortener.models.Account;
import com.example.URL_shortener.repository.RestRepositoryAccount;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountServiceImplIntegrationTest {

    @Autowired
    private RestRepositoryAccount repositoryAccount;

    @Autowired
    private AccountServiceImpl accountService;

    private Account account;

    @AfterEach
    void tearDown() {
        repositoryAccount.deleteAll();
    }

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountId("name");
        account.setPassword("password");
        repositoryAccount.save(account);
    }

    @Test
    void AccountServiceInt_GeneratePassword_NotNull() {
        int length = 7;
        String pass = accountService.generateRandomPassword(length);

        assertThat(pass).isNotNull();
        assertThat(pass.length()).isEqualTo(length);
    }

    @Test
    void AccountServiceInt_CreateAccount_EqualToSave() {
        Account newAccount = new Account();
        newAccount.setAccountId("newName");
        newAccount.setPassword("newPassword");

        accountService.createAccount(newAccount);

        Optional<Account> retrievedAccount = repositoryAccount.findById("newName");
        assertThat(retrievedAccount).isPresent();
        assertThat(retrievedAccount.get().getAccountId()).isEqualTo(newAccount.getAccountId());
        assertThat(retrievedAccount.get().getPassword()).isEqualTo(newAccount.getPassword());
    }

    @Test
    void AccountServiceInt_findAccountById_AccountExists_ReturnsTrue() {
        boolean accountExists = accountService.findAccountById("name");

        assertThat(accountExists).isTrue();
    }

    @Test
    void AccountServiceInt_findAccountById_AccountNotExists_ReturnsFalse() {
        boolean accountExists = accountService.findAccountById("wrongName");

        assertThat(accountExists).isFalse();
    }

    @Test
    void AccountServiceInt_checkAuthorization_ReturnsAccount() {
        Account authenticatedAccount = accountService.checkAuthorization("name", "password");

        assertThat(authenticatedAccount.getAccountId()).isEqualTo(account.getAccountId());
        assertThat(authenticatedAccount.getPassword()).isEqualTo(account.getPassword());
    }

    @Test
    void AccountServiceInt_checkAuthorization_ThrowsAuthorizationErrorException() {
        assertThatThrownBy(() -> accountService.checkAuthorization("wrongName", "wrongPassword"))
                .isInstanceOf(AuthorizationErrorException.class)
                .hasMessageContaining("");
    }

    @Test
    void AccountServiceInt_Authenticate_ReturnsCredentials() {
        String accountId = "testName";
        String password = "testPassword";
        Account testAccount = new Account();
        testAccount.setAccountId(accountId);
        testAccount.setPassword(password);
        repositoryAccount.save(testAccount);

        String authorization = "Basic " + java.util.Base64.getEncoder().encodeToString((accountId + ":" + password).getBytes());

        String[] credentials = accountService.authenticate(authorization);

        assertThat(credentials).isNotNull();
        assertThat(credentials).containsExactly(accountId, password);
    }

    @Test
    void AccountServiceInt_Authenticate_InvalidCredentials_ThrowsAuthorizationErrorException() {
        String invalidAuthorization = "Basic " + Base64.encodeBase64String("invalidUsername:invalidPassword".getBytes());

        assertThatThrownBy(() -> accountService.authenticate(invalidAuthorization))
                .isInstanceOf(AuthorizationErrorException.class)
                .hasMessageContaining("You are not authorized to have access to URL shortening");
    }

    @Test
    void AccountServiceInt_Authenticate_AuthNotStartWithBasic_ThrowsHeaderErrorException() {
        String invalidAuthorization = " " + Base64.encodeBase64String("invalidUsername:invalidPassword".getBytes());

        assertThatThrownBy(() -> accountService.authenticate(invalidAuthorization))
                .isInstanceOf(HeaderErrorException.class)
                .hasMessageContaining("Invalid Authorization header value");
    }

    @Test
    void AccountServiceInt_Authenticate_MoreCredentialsThanNeeded_ThrowsHeaderErrorException() {
        String invalidAuthorization = "Basic " + Base64.encodeBase64String("invalidUsername:invalidPassword:SD".getBytes());

        assertThatThrownBy(() -> accountService.authenticate(invalidAuthorization))
                .isInstanceOf(HeaderErrorException.class)
                .hasMessageContaining("Invalid Authorization header value");
    }

    @Test
    void AccountServiceInt_Authenticate_AccountNULL_AuthorizationErrorException() {
        String invalidAuthorization = "Basic " + Base64.encodeBase64String("invalidUsername:invalidPassword".getBytes());

        assertThatThrownBy(() -> accountService.authenticate(invalidAuthorization))
                .isInstanceOf(AuthorizationErrorException.class)
                .hasMessageContaining("You are not authorized to have access to URL shortening");
    }
}
