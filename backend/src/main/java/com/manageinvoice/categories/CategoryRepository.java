package com.manageinvoice.categories;
import java.util.List; import java.util.Optional; import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByWorkspaceIdAndActiveTrue(UUID workspaceId);
    Optional<Category> findByWorkspaceIdAndId(UUID workspaceId, UUID id);
}
