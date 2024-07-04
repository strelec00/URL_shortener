package com.example.URL_shortener.models;

import jakarta.persistence.*;

@Entity
@Table(name = "url")
public class URL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "accountId")
    private String accountId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "shortened_url", unique = true)
    private String shortenedUrl;

    @Column(name = "redirectType", nullable = false)
    private Integer redirectType;

    public URL() {

    }

    public URL(String accountId, String url, String shortenedUrl, Integer redirectType) {
        this.accountId = accountId;
        this.url = url;
        this.shortenedUrl = shortenedUrl;
        this.redirectType = redirectType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }

    public Integer getRedirectType() {
        return redirectType;
    }
    public void setRedirectType(Integer redirectType) {
        this.redirectType = redirectType;
    }
}
