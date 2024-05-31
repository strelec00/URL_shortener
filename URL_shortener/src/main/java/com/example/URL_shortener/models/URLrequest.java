package com.example.URL_shortener.models;


import com.example.URL_shortener.exceptions.RedirectTypeException;
import com.example.URL_shortener.exceptions.URLIsNullException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class URLrequest {
    private String url;
    private Integer redirectType;

    public URLrequest() {
        this.redirectType = 302;
    }


    @JsonCreator
    public URLrequest(@JsonProperty("url") String url, @JsonProperty("redirectType") Integer redirectType) {
        if (redirectType == null) {
            this.redirectType = 302;
        } else if (redirectType != 302 && redirectType != 301) {
            throw new RedirectTypeException("RedirectType can ONLY be 301 | 302");
        } else {
            this.redirectType = redirectType;
        }
        if (url == null || url.isEmpty()) {
            throw new URLIsNullException("URL cannot be null");
        }
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRedirectType() {
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
