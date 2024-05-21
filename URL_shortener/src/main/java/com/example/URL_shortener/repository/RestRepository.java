package com.example.URL_shortener.repository;

import com.example.URL_shortener.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestRepository extends JpaRepository<Account, String> {
}
