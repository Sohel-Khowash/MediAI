package com.sohel.healthcare.rag.qdrant;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    public List<String> search(List<Float> embedding) {

        var body = new java.util.HashMap<String, Object>();

        body.put("vector", embedding);
        body.put("limit", 3);
        body.put("with_payload", true);

        String response = webClient.post()
                .uri(url() + "/collections/" + collection + "/points/query")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {

            com.fasterxml.jackson.databind.JsonNode root =
                    new com.fasterxml.jackson.databind.ObjectMapper()
                            .readTree(response);

            List<String> chunks = new java.util.ArrayList<>();

            for (JsonNode point : root.path("result").path("points")) {

                chunks.add(
                        point.path("payload")
                                .path("text")
                                .asText()
                );

            }

            return chunks;

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}