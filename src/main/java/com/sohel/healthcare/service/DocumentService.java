package com.sohel.healthcare.service;

import com.sohel.healthcare.dto.DocumentResponse;
import com.sohel.healthcare.entity.Document;
import com.sohel.healthcare.rag.Chunk;
import com.sohel.healthcare.rag.PdfExtractionService;
import com.sohel.healthcare.rag.TextChunker;
import com.sohel.healthcare.repository.DocumentRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final TextChunker textChunker;
    private final PdfExtractionService pdfExtractionService;
    private final MinioClient minioClient;
    private final DocumentRepository documentRepository;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public List<Chunk> chunks(Long id){

        String text = extract(id);

        return textChunker.chunk(text);

    }

    public String extract(Long id) {

        Document document =
                documentRepository.findById(id)
                        .orElseThrow();

        return pdfExtractionService.extractText(
                document.getObjectName()
        );
    }

    public DocumentResponse uploadDocument(MultipartFile file, String uploadedBy) {

        try {

            String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            Document document = Document.builder()
                    .fileName(file.getOriginalFilename())
                    .objectName(objectName)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .uploadedAt(LocalDateTime.now())
                    .uploadedBy(uploadedBy)
                    .build();

            document = documentRepository.save(document);

            return DocumentResponse.builder()
                    .id(document.getId())
                    .fileName(document.getFileName())
                    .fileSize(document.getFileSize())
                    .contentType(document.getContentType())
                    .uploadedAt(document.getUploadedAt().toString())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
}