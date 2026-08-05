package com.sohel.healthcare.controller;

import com.sohel.healthcare.rag.chat.ChatRequest;
import com.sohel.healthcare.rag.chat.ChatResponse;
import com.sohel.healthcare.service.LlmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final LlmService llmService;

    @PostMapping
    public ChatResponse chat(
            @RequestBody ChatRequest request) {

        String answer =
                llmService.chat(request.getQuestion());

        return ChatResponse.builder()
                .answer(answer)
                .build();
    }

    @GetMapping("/test")
    public String test() {
        return llmService.chat(
                "Say hello in exactly one sentence."
        );
    }
}