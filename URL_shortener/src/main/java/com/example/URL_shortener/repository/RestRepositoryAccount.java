package com.example.URL_shortener.repository;

import com.example.URL_shortener.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestRepositoryAccount extends JpaRepository<Account, String> {
    Account findByAccountIdAndPassword(String accountId, String password);
}
