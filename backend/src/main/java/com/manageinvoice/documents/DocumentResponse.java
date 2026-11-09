package com.manageinvoice.documents;

import java.time.Instant;
import java.util.UUID;

public record DocumentResponse(UUID id, String originalFilename, String mimeType, long fileSize,
                               String checksum, DocumentStatus processingStatus, Instant uploadedAt,
                               boolean archived, boolean deleted) {
    public DocumentResponse(Document document) {
        this(document.getId(), document.getOriginalFilename(), document.getMimeType(), document.getFileSize(),
            document.getChecksum(), document.getProcessingStatus(), document.getUploadedAt(),
            document.getArchivedAt() != null, document.getDeletedAt() != null);
    }
}
