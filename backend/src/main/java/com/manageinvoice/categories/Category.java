package com.manageinvoice.categories;
import java.time.Instant; import java.util.UUID;
import jakarta.persistence.*;
@Entity @Table(name = "categories")
public class Category {
    @Id private UUID id;
    @Column(name = "workspace_id", nullable = false) private UUID workspaceId;
    @Column(nullable = false) private String name; private String description; private String color;
    @Column(name = "category_type", nullable = false) private String categoryType = "EXPENSE";
    @Column(name = "is_system", nullable = false) private boolean system;
    @Column(name = "is_active", nullable = false) private boolean active = true;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;
    @PrePersist void create() { id = id == null ? UUID.randomUUID() : id; createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate void update() { updatedAt = Instant.now(); }
    public UUID getId() { return id; } public UUID getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(UUID workspaceId) { this.workspaceId = workspaceId; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public String getColor() { return color; } public void setColor(String color) { this.color = color; }
    public boolean isActive() { return active; } public void setActive(boolean active) { this.active = active; }
}
