package com.manageinvoice.documents;
import java.math.BigDecimal; import java.util.*; import org.springframework.security.core.annotation.AuthenticationPrincipal; import org.springframework.web.bind.annotation.*; import com.manageinvoice.common.security.AuthenticatedUser; import com.manageinvoice.tenancy.WorkspaceService;
@RestController @RequestMapping("/api/v1/document-relationships") public class DocumentRelationshipController {
 private final DocumentRelationshipRepository relationships; private final WorkspaceService workspaces;
 public DocumentRelationshipController(DocumentRelationshipRepository relationships,WorkspaceService workspaces){this.relationships=relationships;this.workspaces=workspaces;}
 @GetMapping public List<DocumentRelationship> list(@AuthenticationPrincipal AuthenticatedUser user,@RequestHeader("X-Workspace-Id") UUID workspaceId){workspaces.requireMembership(workspaceId,user.id());return relationships.findByWorkspaceId(workspaceId);}
 @PostMapping public DocumentRelationship create(@AuthenticationPrincipal AuthenticatedUser user,@RequestHeader("X-Workspace-Id") UUID workspaceId,@RequestBody RelationshipRequest request){workspaces.requireMembership(workspaceId,user.id());DocumentRelationship relationship=new DocumentRelationship();relationship.setWorkspaceId(workspaceId);relationship.setSourceDocumentId(request.sourceDocumentId());relationship.setRelatedDocumentId(request.relatedDocumentId());relationship.setRelationshipType(request.relationshipType());relationship.setConfidenceScore(request.confidenceScore());return relationships.save(relationship);}
 public record RelationshipRequest(UUID sourceDocumentId,UUID relatedDocumentId,String relationshipType,BigDecimal confidenceScore){}
}
