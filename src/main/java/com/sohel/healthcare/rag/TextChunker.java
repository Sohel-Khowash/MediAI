package com.sohel.healthcare.rag;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TextChunker {

    private static final int CHUNK_SIZE = 800;

    private static final int OVERLAP = 150;

    public List<Chunk> chunk(String text){

        List<Chunk> chunks = new ArrayList<>();

        int start = 0;

        int index = 0;

        while(start < text.length()){

            int end = Math.min(start + CHUNK_SIZE, text.length());

            String chunk =
                    text.substring(start, end);

            chunks.add(
                    Chunk.builder()
                            .index(index++)
                            .text(chunk)
                            .build()
            );

            start += CHUNK_SIZE - OVERLAP;
        }

        return chunks;
    }

}