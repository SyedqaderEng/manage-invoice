package com.manageinvoice.documents;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    Optional<Document> findByWorkspaceIdAndIdAndDeletedAtIsNull(UUID workspaceId, UUID id);
    Optional<Document> findFirstByWorkspaceIdAndChecksumAndDeletedAtIsNull(UUID workspaceId, String checksum);
    Page<Document> findByWorkspaceIdAndDeletedAtIsNull(UUID workspaceId, Pageable pageable);
    List<Document> findAllByWorkspaceIdAndDeletedAtIsNull(UUID workspaceId);
}
