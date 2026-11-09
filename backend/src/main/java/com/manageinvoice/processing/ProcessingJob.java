package com.manageinvoice.processing;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "processing_jobs")
public class ProcessingJob {
    @Id private UUID id;
    @Column(name = "workspace_id", nullable = false) private UUID workspaceId;
    @Column(name = "document_id", nullable = false) private UUID documentId;
    @Column(name = "job_type", nullable = false) private String jobType;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private ProcessingJobStatus status;
    @Column(name = "attempt_count", nullable = false) private int attemptCount;
    @Column(name = "error_code") private String errorCode;
    @Column(name = "error_message") private String errorMessage;
    @Column(name = "started_at") private Instant startedAt;
    @Column(name = "completed_at") private Instant completedAt;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;

    @PrePersist void onCreate() { id = id == null ? UUID.randomUUID() : id; createdAt = Instant.now(); }

    public UUID getId() { return id; }
    public UUID getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(UUID workspaceId) { this.workspaceId = workspaceId; }
    public UUID getDocumentId() { return documentId; }
    public void setDocumentId(UUID documentId) { this.documentId = documentId; }
    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public ProcessingJobStatus getStatus() { return status; }
    public void setStatus(ProcessingJobStatus status) { this.status = status; }
    public int getAttemptCount() { return attemptCount; }
    public void incrementAttempt() { attemptCount++; startedAt = Instant.now(); }
    public String getErrorCode() { return errorCode; }
    public void fail(String code, String message) { status = ProcessingJobStatus.FAILED; errorCode = code; errorMessage = message; completedAt = Instant.now(); }
    public void complete() { status = ProcessingJobStatus.COMPLETED; completedAt = Instant.now(); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getCompletedAt() { return completedAt; }
}
