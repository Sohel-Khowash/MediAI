package com.sohel.healthcare.service;

import com.sohel.healthcare.rag.Chunk;
import com.sohel.healthcare.rag.PdfExtractionService;
import com.sohel.healthcare.rag.TextChunker;
import com.sohel.healthcare.rag.qdrant.QdrantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexingService {

    private final PdfExtractionService pdfExtractionService;
    private final TextChunker textChunker;
    private final LlmService llmService;
    private final QdrantService qdrantService;

    public void indexDocument(String objectName) {


        String text = pdfExtractionService.extractText(objectName);

        List<Chunk> chunks = textChunker.chunk(text);


        for (Chunk chunk : chunks) {


            qdrantService.storeChunk(
                    llmService.embedding(chunk.getText()),
                    chunk.getText()
            );

        }

    }
}