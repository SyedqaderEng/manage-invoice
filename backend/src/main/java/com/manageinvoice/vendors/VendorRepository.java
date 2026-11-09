package com.manageinvoice.vendors;
import java.util.List; import java.util.Optional; import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VendorRepository extends JpaRepository<Vendor, UUID> {
    List<Vendor> findByWorkspaceId(UUID workspaceId);
    Optional<Vendor> findByWorkspaceIdAndId(UUID workspaceId, UUID id);
    Optional<Vendor> findByWorkspaceIdAndNormalizedName(UUID workspaceId, String normalizedName);
}
