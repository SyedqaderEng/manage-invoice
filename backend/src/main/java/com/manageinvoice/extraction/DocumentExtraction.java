package com.manageinvoice.extraction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "document_extractions")
public class DocumentExtraction {
    @Id private UUID id;
    @Column(name = "workspace_id", nullable = false) private UUID workspaceId;
    @Column(name = "document_id", nullable = false) private UUID documentId;
    @Column(name = "extraction_version", nullable = false) private int extractionVersion;
    @Column(name = "raw_extraction_json", nullable = false, columnDefinition = "text") private String rawExtractionJson;
    @Column(name = "normalized_data_json", nullable = false, columnDefinition = "text") private String normalizedDataJson;
    @Column(name = "overall_confidence", precision = 5, scale = 4) private BigDecimal overallConfidence;
    @Column(name = "model_provider") private String modelProvider;
    @Column(name = "model_name") private String modelName;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;

    @PrePersist void onCreate() { id = id == null ? UUID.randomUUID() : id; createdAt = Instant.now(); }

    public UUID getId() { return id; }
    public UUID getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(UUID workspaceId) { this.workspaceId = workspaceId; }
    public UUID getDocumentId() { return documentId; }
    public void setDocumentId(UUID documentId) { this.documentId = documentId; }
    public int getExtractionVersion() { return extractionVersion; }
    public void setExtractionVersion(int extractionVersion) { this.extractionVersion = extractionVersion; }
    public String getRawExtractionJson() { return rawExtractionJson; }
    public void setRawExtractionJson(String rawExtractionJson) { this.rawExtractionJson = rawExtractionJson; }
    public String getNormalizedDataJson() { return normalizedDataJson; }
    public void setNormalizedDataJson(String normalizedDataJson) { this.normalizedDataJson = normalizedDataJson; }
    public BigDecimal getOverallConfidence() { return overallConfidence; }
    public void setOverallConfidence(BigDecimal overallConfidence) { this.overallConfidence = overallConfidence; }
    public String getModelProvider() { return modelProvider; }
    public void setModelProvider(String modelProvider) { this.modelProvider = modelProvider; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
}
