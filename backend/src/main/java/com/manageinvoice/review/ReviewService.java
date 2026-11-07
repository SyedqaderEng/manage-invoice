package com.manageinvoice.review;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manageinvoice.documents.Document;
import com.manageinvoice.documents.DocumentRepository;
import com.manageinvoice.documents.DocumentStatus;
import com.manageinvoice.extraction.DocumentExtraction;
import com.manageinvoice.extraction.DocumentExtractionRepository;
import com.manageinvoice.identity.UserRepository;
import com.manageinvoice.tenancy.WorkspaceMember;
import com.manageinvoice.tenancy.WorkspaceMemberRepository;
import com.manageinvoice.tenancy.WorkspaceRole;
import com.manageinvoice.tenancy.WorkspaceStatus;

@Service
public class ReviewService {
    private final DocumentRepository documents;
    private final DocumentExtractionRepository extractions;
    private final DocumentReviewRepository reviews;
    private final WorkspaceMemberRepository members;
    private final UserRepository users;
    private final ObjectMapper objectMapper;

    public ReviewService(DocumentRepository documents, DocumentExtractionRepository extractions,
                         DocumentReviewRepository reviews, WorkspaceMemberRepository members,
                         UserRepository users, ObjectMapper objectMapper) {
        this.documents = documents;
        this.extractions = extractions;
        this.reviews = reviews;
        this.members = members;
        this.users = users;
        this.objectMapper = objectMapper;
    }

    public ReviewState get(UUID workspaceId, UUID userId, UUID documentId) {
        Document document = requireDocument(workspaceId, userId, documentId);
        DocumentExtraction extraction = requireExtraction(workspaceId, documentId);
        return new ReviewState(document.getId(), document.getProcessingStatus(), latestOriginal(workspaceId, documentId), extraction.getNormalizedDataJson());
    }

    @Transactional
    public ReviewState save(UUID workspaceId, UUID userId, UUID documentId, Map<String, Object> correctedData, String notes) {
        requireRole(workspaceId, userId, WorkspaceRole.VIEWER, false);
        return record(workspaceId, userId, documentId, correctedData, notes, ReviewStatus.SAVED);
    }

    @Transactional
    public ReviewState approve(UUID workspaceId, UUID userId, UUID documentId, Map<String, Object> correctedData, String notes) {
        requireRole(workspaceId, userId, WorkspaceRole.ADMIN, true);
        ReviewState state = record(workspaceId, userId, documentId, correctedData, notes, ReviewStatus.APPROVED);
        Document document = requireDocument(workspaceId, userId, documentId);
        document.setProcessingStatus(DocumentStatus.APPROVED);
        documents.save(document);
        return state;
    }

    @Transactional
    public ReviewState reject(UUID workspaceId, UUID userId, UUID documentId, String notes) {
        requireRole(workspaceId, userId, WorkspaceRole.ADMIN, true);
        Document document = requireDocument(workspaceId, userId, documentId);
        DocumentExtraction extraction = requireExtraction(workspaceId, documentId);
        DocumentReview review = createReview(workspaceId, userId, documentId, extraction.getNormalizedDataJson(), notes, ReviewStatus.REJECTED);
        reviews.save(review);
        document.setProcessingStatus(DocumentStatus.REVIEW_REQUIRED);
        documents.save(document);
        return new ReviewState(document.getId(), document.getProcessingStatus(), extraction.getNormalizedDataJson(), extraction.getNormalizedDataJson());
    }

    private ReviewState record(UUID workspaceId, UUID userId, UUID documentId, Map<String, Object> correctedData,
                               String notes, ReviewStatus status) {
        DocumentExtraction extraction = requireExtraction(workspaceId, documentId);
        try {
            String correctedJson = objectMapper.writeValueAsString(correctedData);
            JsonNode corrected = objectMapper.readTree(correctedJson);
            if (correctedData.isEmpty() || corrected.get("total") == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Corrected extraction must include a total");
            }
            DocumentReview review = createReview(workspaceId, userId, documentId, correctedJson, notes, status);
            reviews.save(review);
            Document document = requireDocument(workspaceId, userId, documentId);
            return new ReviewState(document.getId(), document.getProcessingStatus(), extraction.getNormalizedDataJson(), correctedJson);
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid corrected extraction", exception);
        }
    }

    private DocumentReview createReview(UUID workspaceId, UUID userId, UUID documentId, String correctedJson,
                                        String notes, ReviewStatus status) {
        DocumentExtraction extraction = requireExtraction(workspaceId, documentId);
        DocumentReview review = new DocumentReview();
        review.setWorkspaceId(workspaceId);
        review.setDocumentId(documentId);
        review.setReviewer(users.findById(userId).orElseThrow());
        review.setReviewStatus(status);
        review.setOriginalDataJson(extraction.getNormalizedDataJson());
        review.setCorrectedDataJson(correctedJson);
        review.setReviewNotes(notes);
        return review;
    }

    private Document requireDocument(UUID workspaceId, UUID userId, UUID documentId) {
        requireRole(workspaceId, userId, null, false);
        return documents.findByWorkspaceIdAndIdAndDeletedAtIsNull(workspaceId, documentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
    }

    private DocumentExtraction requireExtraction(UUID workspaceId, UUID documentId) {
        return extractions.findFirstByWorkspaceIdAndDocumentIdOrderByExtractionVersionDesc(workspaceId, documentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Extraction not found"));
    }

    private WorkspaceMember requireRole(UUID workspaceId, UUID userId, WorkspaceRole minimum, boolean approval) {
        WorkspaceMember member = members.findByWorkspaceIdAndUserIdAndStatus(workspaceId, userId, WorkspaceStatus.ACTIVE)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Workspace access denied"));
        if (member.getRole() == WorkspaceRole.VIEWER && (minimum != null || approval)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Viewer role cannot review documents");
        }
        if (approval && member.getRole() != WorkspaceRole.OWNER && member.getRole() != WorkspaceRole.ADMIN && member.getRole() != WorkspaceRole.REVIEWER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Approval permission required");
        }
        return member;
    }

    private String latestOriginal(UUID workspaceId, UUID documentId) {
        return requireExtraction(workspaceId, documentId).getNormalizedDataJson();
    }

    public record ReviewState(UUID documentId, DocumentStatus status, String originalData, String correctedData) {}
}
