package com.manageinvoice.expenses;
import java.util.Optional; import java.util.UUID;
import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExpenseRepository extends JpaRepository<ExpenseTransaction, UUID> {
    Page<ExpenseTransaction> findByWorkspaceId(UUID workspaceId, Pageable pageable);
    Optional<ExpenseTransaction> findByWorkspaceIdAndId(UUID workspaceId, UUID id);
    Optional<ExpenseTransaction> findByWorkspaceIdAndDocumentId(UUID workspaceId, UUID documentId);
}
