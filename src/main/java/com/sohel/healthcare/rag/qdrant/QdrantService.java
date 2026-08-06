package com.sohel.healthcare.rag.qdrant;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QdrantService {

    private final WebClient webClient;

    @Value("${qdrant.host}")
    private String host;

    @Value("${qdrant.port}")
    private int port;

    @Value("${qdrant.collection}")
    private String collection;

    private String url() {

        return "http://" + host + ":" + port;

    }

    public String health() {

        return webClient.get()
                .uri(url())
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }

    public void createCollection() {

        String body = """
                {
                  "vectors": {
                    "size": 768,
                    "distance": "Cosine"
                  }
                }
                """;

        webClient.put()
                .uri(url() + "/collections/" + collection)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }

    public void storeChunk(List<Float> embedding, String text) {

        String id = UUID.randomUUID().toString();

        Map<String, Object> payload = new HashMap<>();
        payload.put("text", text);

        Map<String, Object> point = new HashMap<>();
        point.put("id", id);
        point.put("vector", embedding);
        point.put("payload", payload);

        Map<String, Object> body = new HashMap<>();
        body.put("points", List.of(point));

        webClient.put()
                .uri(url() + "/collections/" + collection + "/points")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}