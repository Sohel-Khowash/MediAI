package com.sohel.healthcare.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LlmService {

    private final WebClient webClient;

    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${llm.embedding-model}")
    private String embeddingModel;

    @Value("${llm.base-url}")
    private String baseUrl;

    @Value("${llm.chat-model}")
    private String model;

    public String chat(String prompt) {

        try {

            System.out.println("Sending request to Ollama...");

            String request = """
                {
                    "model":"%s",
                    "prompt":"%s",
                    "stream":false
                }
                """.formatted(model, prompt.replace("\"", "\\\""));

            String response = webClient.post()
                    .uri(baseUrl + "/api/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnNext(r -> System.out.println("Response received"))
                    .block();

            System.out.println(response);

            JsonNode root = mapper.readTree(response);

            return root.get("response").asText();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Float> embedding(String text) {

        try {

            String request = """
                {
                    "model":"%s",
                    "input":"%s"
                }
                """
                    .formatted(
                            embeddingModel,
                            text.replace("\"","\\\"")
                    );

            String response = webClient.post()
                    .uri(baseUrl + "/api/embed")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = mapper.readTree(response);

            JsonNode array =
                    root.get("embeddings").get(0);

            List<Float> vector =
                    new ArrayList<>();

            for(JsonNode node : array){

                vector.add(
                        node.floatValue()
                );

            }

            return vector;

        }
        catch (Exception e){

            throw new RuntimeException(e);

        }

    }

}