package com.sohel.healthcare.controller;

import com.sohel.healthcare.dto.DocumentResponse;
import com.sohel.healthcare.rag.Chunk;
import com.sohel.healthcare.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DocumentResponse uploadDocument(
            @RequestParam("file") MultipartFile file
    ) {

        return documentService.uploadDocument(file, "admin");
    }

    @GetMapping("/{id}/text")
    public String extract(@PathVariable Long id){

        return documentService.extract(id);
    }

    @GetMapping("/{id}/chunks")
    public List<Chunk> chunks(
            @PathVariable Long id){

        return documentService.chunks(id);

    }
}