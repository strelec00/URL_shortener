package com.example.URL_shortener.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class URL {
    private String url;
    private int redirectType;

    public URL() {
        this.redirectType = 302;

    }


    @JsonCreator
    public URL(@JsonProperty("url") String url, @JsonProperty("redirectType") int redirectType) {

        if (redirectType != 302 && redirectType != 301) {
            throw new IllegalArgumentException("redirectType must be 302 or 301");
        }
        this.url = url;
        this.redirectType = redirectType;
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
