package com.manageinvoice.invoices;
import java.util.Optional; import java.util.UUID;
import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    Page<Invoice> findByWorkspaceId(UUID workspaceId, Pageable pageable);
    Optional<Invoice> findByWorkspaceIdAndId(UUID workspaceId, UUID id);
}
