package com.manageinvoice.vendors;

import java.time.Instant;
import java.util.UUID;
import com.manageinvoice.categories.Category;
import jakarta.persistence.*;

@Entity @Table(name = "vendors")
public class Vendor {
    @Id private UUID id;
    @Column(name = "workspace_id", nullable = false) private UUID workspaceId;
    @Column(name = "normalized_name", nullable = false) private String normalizedName;
    @Column(name = "display_name", nullable = false) private String displayName;
    private String email; private String phone; private String address;
    @Column(name = "tax_identifier_encrypted") private String taxIdentifierEncrypted;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "default_category_id") private Category defaultCategory;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;
    @PrePersist void create() { id = id == null ? UUID.randomUUID() : id; createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate void update() { updatedAt = Instant.now(); }
    public UUID getId() { return id; }
    public UUID getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(UUID workspaceId) { this.workspaceId = workspaceId; }
    public String getNormalizedName() { return normalizedName; }
    public void setNormalizedName(String normalizedName) { this.normalizedName = normalizedName; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
