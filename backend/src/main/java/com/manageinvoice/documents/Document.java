package com.manageinvoice.documents;

import java.time.Instant;
import java.util.UUID;

import com.manageinvoice.identity.User;
import com.manageinvoice.tenancy.Workspace;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "documents")
public class Document {
    @Id private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "workspace_id", nullable = false) private Workspace workspace;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "uploaded_by", nullable = false) private User uploadedBy;
    @Column(name = "original_filename", nullable = false) private String originalFilename;
    @Column(name = "storage_key", nullable = false, unique = true) private String storageKey;
    @Column(name = "mime_type", nullable = false) private String mimeType;
    @Column(name = "file_size", nullable = false) private long fileSize;
    @Column(nullable = false, length = 64) private String checksum;
    @Enumerated(EnumType.STRING) @Column(name = "document_type", nullable = false) private DocumentType documentType = DocumentType.UNKNOWN;
    @Enumerated(EnumType.STRING) @Column(name = "processing_status", nullable = false) private DocumentStatus processingStatus = DocumentStatus.UPLOADED;
    @Column(name = "review_status", nullable = false) private String reviewStatus = "NOT_REQUIRED";
    @Column(name = "uploaded_at", nullable = false, updatable = false) private Instant uploadedAt;
    @Column(name = "processed_at") private Instant processedAt;
    @Column(name = "archived_at") private Instant archivedAt;
    @Column(name = "deleted_at") private Instant deletedAt;

    @PrePersist void onCreate() { id = id == null ? UUID.randomUUID() : id; uploadedAt = Instant.now(); }

    public UUID getId() { return id; }
    public Workspace getWorkspace() { return workspace; }
    public void setWorkspace(Workspace workspace) { this.workspace = workspace; }
    public User getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(User uploadedBy) { this.uploadedBy = uploadedBy; }
    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }
    public String getStorageKey() { return storageKey; }
    public void setStorageKey(String storageKey) { this.storageKey = storageKey; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }
    public DocumentStatus getProcessingStatus() { return processingStatus; }
    public Instant getUploadedAt() { return uploadedAt; }
    public Instant getArchivedAt() { return archivedAt; }
    public Instant getDeletedAt() { return deletedAt; }
    public void archive() { archivedAt = Instant.now(); processingStatus = DocumentStatus.ARCHIVED; }
    public void setProcessingStatus(DocumentStatus processingStatus) { this.processingStatus = processingStatus; }
    public void softDelete() { deletedAt = Instant.now(); }
}
