package com.sohel.healthcare.service;

import com.sohel.healthcare.rag.qdrant.QdrantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RagService {

    private final LlmService llmService;
    private final QdrantService qdrantService;

    public String ask(String question) {

        List<Float> embedding =
                llmService.embedding(question);

        List<String> chunks =
                qdrantService.search(embedding);

        String context = String.join("\n\n", chunks);

        String prompt = """
                You are a helpful AI assistant.

                Answer ONLY using the provided context.

                If the answer is not present in the context, reply:

                "I could not find the answer in the uploaded documents."

                Context:
                %s

                Question:
                %s

                Answer:
                """.formatted(context, question);

        return llmService.chat(prompt);

    }

}