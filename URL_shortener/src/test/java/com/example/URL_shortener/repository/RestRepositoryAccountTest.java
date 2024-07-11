package com.example.URL_shortener.repository;

import com.example.URL_shortener.models.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class RestRepositoryAccountTest {


    @Autowired
    private RestRepositoryAccount restRepository;

    @AfterEach
    void tearDown() {
        restRepository.deleteAll();
    }

    String accountId = "name";
    String password = "password";

    @Test
    void AccountRepository_FindByAccountIdAndPassword_NotNULL() {
        // given
        Account account = new Account(accountId, password);
        restRepository.save(account);

        // when
        Account foundAccount = restRepository.findByAccountIdAndPassword(accountId, password);

        // then
        assertThat(foundAccount).isNotNull();
        assertThat(foundAccount.getAccountId()).isEqualTo(accountId);
        assertThat(foundAccount.getPassword()).isEqualTo(password);
    }

    @Test
    void AccountRepository_DidntFindByAccountIdAndPassword_IsNULL() {
        // when
        Account foundAccount = restRepository.findByAccountIdAndPassword("jan", "knfa913SDA");

        // then
        assertThat(foundAccount).isNull();
    }


}