package com.example.URL_shortener.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "accountId", unique = true, nullable = false)
    private String accountId;

    @Column(name = "password", nullable = false)
    private String password;

    public Account() {
    }

    public Account(@JsonProperty("accountId") String accountId, @JsonProperty("password") String password) {
        this.accountId = accountId;
        this.password = password;
    }


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
