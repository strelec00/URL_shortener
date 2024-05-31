package com.example.URL_shortener.repository;

import com.example.URL_shortener.models.URL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestRepositoryURL extends JpaRepository<URL, Long> {

}
