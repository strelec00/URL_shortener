package com.example.URL_shortener.controller;

import com.example.URL_shortener.exceptions.URLnotFoundException;
import com.example.URL_shortener.models.URL;
import com.example.URL_shortener.models.URLrequest;
import com.example.URL_shortener.services.URLshorteningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RedirectionControllerIntTest {

    @Autowired
    private URLshorteningService urlshorteningService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void RedirectionControllerInt_Redirect_redirectSuccess() throws Exception {
        String originalUrl = "https://example.com";
        int redirectType = 302;

        String shortUrl = urlshorteningService.generateShortURL(new URLrequest(originalUrl, redirectType)).getShortUrl();
        String hash = shortUrl.substring(shortUrl.lastIndexOf("/") + 1);

        URL url = new URL();
        url.setUrl(originalUrl);
        url.setRedirectType(redirectType);
        url.setShortenedUrl(shortUrl);

        urlshorteningService.addURL(url);

        mockMvc.perform(get("/" + hash)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(url.getRedirectType()))
                .andExpect(header().string("Location", url.getUrl()));
    }

    @Test
    void RedirectionControllerInt_Redirect_redirectNotFound() throws Exception {
        String hash = "abc123";
        String generatedUrl = "http://short.url/";

        mockMvc.perform(get("/" + hash)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assertThat(result.getResolvedException()).isInstanceOf(URLnotFoundException.class)
                            .hasMessageContaining("URL not found");
                });
    }
}