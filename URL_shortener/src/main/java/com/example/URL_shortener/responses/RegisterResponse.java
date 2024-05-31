package com.example.URL_shortener.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RegisterResponse {
    private boolean success;

    private String description;

    private String password;

    public RegisterResponse(boolean success, String description) {
        this.success = success;
        this.description = description;
    }

    public RegisterResponse(String password, boolean success) {
        this.password = password;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
