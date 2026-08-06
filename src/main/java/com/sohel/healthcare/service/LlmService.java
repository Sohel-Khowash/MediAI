package com.sohel.healthcare.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LlmService {

    private final WebClient webClient;

    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${llm.base-url}")
    private String baseUrl;

    @Value("${llm.chat-model}")
    private String chatModel;

    @Value("${llm.embedding-model}")
    private String embeddingModel;

    public String chat(String prompt) {

        try {

            Map<String, Object> request = new HashMap<>();
            request.put("model", chatModel);
            request.put("prompt", prompt);
            request.put("stream", false);

            String response = webClient.post()
                    .uri(baseUrl + "/api/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = mapper.readTree(response);

            return root.get("response").asText();

        } catch (Exception e) {
            throw new RuntimeException("Ollama chat failed", e);
        }
    }

    public List<Float> embedding(String text) {

        try {

            Map<String, Object> request = new HashMap<>();
            request.put("model", embeddingModel);
            request.put("input", text);

            String response = webClient.post()
                    .uri(baseUrl + "/api/embed")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = mapper.readTree(response);

            JsonNode embeddings = root.get("embeddings");

            if (embeddings == null || embeddings.isEmpty()) {
                throw new RuntimeException("No embeddings returned from Ollama.");
            }

            JsonNode vectorNode = embeddings.get(0);

            List<Float> vector = new ArrayList<>();

            for (JsonNode value : vectorNode) {
                vector.add(value.floatValue());
            }

            return vector;

        } catch (Exception e) {
            throw new RuntimeException("Ollama embedding failed", e);
        }
    }
}