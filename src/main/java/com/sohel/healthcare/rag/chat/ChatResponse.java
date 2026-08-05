package com.sohel.healthcare.rag.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChatResponse {

    private String answer;

}