package com.manageinvoice.documents;
import java.util.List; import java.util.UUID; import org.springframework.data.jpa.repository.JpaRepository;
public interface DocumentRelationshipRepository extends JpaRepository<DocumentRelationship,UUID>{ List<DocumentRelationship> findByWorkspaceId(UUID workspaceId); }
