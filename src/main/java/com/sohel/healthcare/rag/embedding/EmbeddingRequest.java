package com.sohel.healthcare.rag.embedding;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmbeddingRequest {

    private String model;

    private String input;

}