package com.example.URL_shortener.repository;

import com.example.URL_shortener.models.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class RestRepositoryAccountIntTest {

    @Autowired
    private RestRepositoryAccount repository;

    @Test
    public void AccRepositoryInt_FindByAccountIdAndPassword_returnAcc() {
        // given
        Account account = new Account();
        account.setAccountId("testAccountId");
        account.setPassword("testPassword");
        repository.save(account);

        // when
        Account found = repository.findByAccountIdAndPassword("testAccountId", "testPassword");

        // then
        assertThat(found).isNotNull();
        assertThat(found.getAccountId()).isEqualTo("testAccountId");
    }

    @Test
    public void AccRepositoryInt_FindByAccountIdAndPassword_returnNull() {
        // given
        Account account = new Account();
        account.setAccountId("testAccountId");
        account.setPassword("testPassword");
        repository.save(account);

        // when
        Account found = repository.findByAccountIdAndPassword("wrongAccountId", "wrongPassword");

        // then
        assertThat(found).isNull();
    }

}
