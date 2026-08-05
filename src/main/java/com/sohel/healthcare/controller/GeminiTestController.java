package com.sohel.healthcare.controller;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeminiTestController {

    @Value("${gemini.api-key}")
    private String apiKey;

    @GetMapping("/api/v1/models")
    public Object models() {

        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        return client.models.list();
    }
}