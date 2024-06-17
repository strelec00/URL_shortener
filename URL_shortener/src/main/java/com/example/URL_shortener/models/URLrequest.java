package com.example.URL_shortener.models;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class URLrequest {

    @NotNull(message = "URL cannot be null!")
    @Pattern(regexp = "^(http|https)://.*$", message = "URL must have http or https")
    @Size(min = 12, max = 240, message = "Invalid Name: Must be of 12 - 240 characters")
    private String url;

    private Integer redirectType;

    public URLrequest() {
        this.redirectType = 302;
    }


    @JsonCreator
    public URLrequest(@JsonProperty("url") String url, @JsonProperty("redirectType") Integer redirectType) {
        this.redirectType = redirectType;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(int redirectType) {
        this.redirectType = redirectType;
    }

    @Override
    public String toString() {
        return "URL{" +
                "url='" + url + '\'' +
                ", redirectType=" + redirectType +
                '}';
    }

}
