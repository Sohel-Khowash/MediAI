package com.sohel.healthcare.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponse {

    private Long id;

    private String fileName;

    private Long fileSize;

    private String contentType;

    private String uploadedAt;
}