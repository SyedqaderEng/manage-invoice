package com.manageinvoice.documents;
import java.math.BigDecimal; import java.time.Instant; import java.util.UUID;
import jakarta.persistence.*;
@Entity @Table(name="document_relationships")
public class DocumentRelationship {
 @Id private UUID id; @Column(name="workspace_id",nullable=false) private UUID workspaceId;
 @Column(name="source_document_id",nullable=false) private UUID sourceDocumentId; @Column(name="related_document_id",nullable=false) private UUID relatedDocumentId;
 @Column(name="relationship_type",nullable=false) private String relationshipType;
 @Column(name="confidence_score",nullable=false,precision=5,scale=4) private BigDecimal confidenceScore;
 @Column(name="confirmation_status",nullable=false) private String confirmationStatus="PENDING";
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
 @PrePersist void create(){id=id==null?UUID.randomUUID():id;createdAt=Instant.now();}
 public UUID getId(){return id;} public UUID getWorkspaceId(){return workspaceId;} public void setWorkspaceId(UUID workspaceId){this.workspaceId=workspaceId;}
 public UUID getSourceDocumentId(){return sourceDocumentId;} public void setSourceDocumentId(UUID sourceDocumentId){this.sourceDocumentId=sourceDocumentId;}
 public UUID getRelatedDocumentId(){return relatedDocumentId;} public void setRelatedDocumentId(UUID relatedDocumentId){this.relatedDocumentId=relatedDocumentId;}
 public String getRelationshipType(){return relationshipType;} public void setRelationshipType(String relationshipType){this.relationshipType=relationshipType;}
 public BigDecimal getConfidenceScore(){return confidenceScore;} public void setConfidenceScore(BigDecimal confidenceScore){this.confidenceScore=confidenceScore;}
 public String getConfirmationStatus(){return confirmationStatus;} public void setConfirmationStatus(String confirmationStatus){this.confirmationStatus=confirmationStatus;}
}
