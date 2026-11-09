package com.manageinvoice.review;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentReviewRepository extends JpaRepository<DocumentReview, UUID> {
    List<DocumentReview> findByWorkspaceIdAndDocumentIdOrderByReviewedAtDesc(UUID workspaceId, UUID documentId);
}
