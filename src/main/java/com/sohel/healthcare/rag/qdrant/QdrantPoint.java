package com.sohel.healthcare.rag.qdrant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QdrantPoint {

    private String id;

    private List<Float> vector;

    private Map<String, Object> payload;

}