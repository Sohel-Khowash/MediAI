package com.sohel.healthcare.controller;

import com.sohel.healthcare.rag.qdrant.QdrantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/qdrant")
@RequiredArgsConstructor
public class QdrantController {

    private final QdrantService qdrantService;

    @GetMapping("/test")
    public String test() {

        return qdrantService.health();

    }

    @PostMapping("/create")
    public String create() {

        qdrantService.createCollection();

        return "Collection Created";

    }

}