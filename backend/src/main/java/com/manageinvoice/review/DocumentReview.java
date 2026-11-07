package com.manageinvoice.review;

import java.time.Instant;
import java.util.UUID;

import com.manageinvoice.identity.User;

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
@Table(name = "document_reviews")
public class DocumentReview {
    @Id private UUID id;
    @Column(name = "workspace_id", nullable = false) private UUID workspaceId;
    @Column(name = "document_id", nullable = false) private UUID documentId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "reviewer_user_id", nullable = false) private User reviewer;
    @Enumerated(EnumType.STRING) @Column(name = "review_status", nullable = false) private ReviewStatus reviewStatus;
    @Column(name = "original_data_json", nullable = false, columnDefinition = "text") private String originalDataJson;
    @Column(name = "corrected_data_json", nullable = false, columnDefinition = "text") private String correctedDataJson;
    @Column(name = "review_notes", columnDefinition = "text") private String reviewNotes;
    @Column(name = "reviewed_at", nullable = false) private Instant reviewedAt;

    @PrePersist void onCreate() { id = id == null ? UUID.randomUUID() : id; reviewedAt = Instant.now(); }

    public UUID getId() { return id; }
    public UUID getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(UUID workspaceId) { this.workspaceId = workspaceId; }
    public UUID getDocumentId() { return documentId; }
    public void setDocumentId(UUID documentId) { this.documentId = documentId; }
    public User getReviewer() { return reviewer; }
    public void setReviewer(User reviewer) { this.reviewer = reviewer; }
    public ReviewStatus getReviewStatus() { return reviewStatus; }
    public void setReviewStatus(ReviewStatus reviewStatus) { this.reviewStatus = reviewStatus; }
    public String getOriginalDataJson() { return originalDataJson; }
    public void setOriginalDataJson(String originalDataJson) { this.originalDataJson = originalDataJson; }
    public String getCorrectedDataJson() { return correctedDataJson; }
    public void setCorrectedDataJson(String correctedDataJson) { this.correctedDataJson = correctedDataJson; }
    public String getReviewNotes() { return reviewNotes; }
    public void setReviewNotes(String reviewNotes) { this.reviewNotes = reviewNotes; }
    public Instant getReviewedAt() { return reviewedAt; }
}
