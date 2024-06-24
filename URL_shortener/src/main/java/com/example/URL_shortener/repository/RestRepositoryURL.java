package com.example.URL_shortener.repository;
import com.example.URL_shortener.models.URL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestRepositoryURL extends JpaRepository<URL, Long> {
    List<URL> findAllByAccountId(String accountId);

    URL findByShortenedUrl(String shortUrl);

}
