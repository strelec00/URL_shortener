package com.example.URL_shortener.repository;

import com.example.URL_shortener.models.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class RestRepositoryAccountUnitTest {

    @Autowired
    private RestRepositoryAccount restRepository;

    @BeforeEach
    void setUp() {
        restRepository.deleteAll();

        Account account = new Account("name", "password");
        restRepository.save(account);
    }


    @Test
    void AccRepositoryUnit_FindByAccountIdAndPassword_returnAcc() {
        // when
        Account foundAccount = restRepository.findByAccountIdAndPassword("name", "password");

        // then
        assertThat(foundAccount).isNotNull();
        assertThat(foundAccount.getAccountId()).isEqualTo("name");
        assertThat(foundAccount.getPassword()).isEqualTo("password");
    }

    @Test
    void AccRepository_DidntFindByAccountIdAndPassword_returnNull() {
        // when
        Account foundAccount = restRepository.findByAccountIdAndPassword("jan", "knfa913SDA");

        // then
        assertThat(foundAccount).isNull();
    }


}