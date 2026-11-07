package com.manageinvoice.expenses;
import java.math.BigDecimal; import java.time.Instant; import java.time.LocalDate; import java.util.UUID;
import jakarta.persistence.*;
@Entity @Table(name = "expense_transactions")
public class ExpenseTransaction {
    @Id private UUID id;
    @Column(name = "workspace_id", nullable = false) private UUID workspaceId;
    @Column(name = "document_id") private UUID documentId; @Column(name = "vendor_id") private UUID vendorId; @Column(name = "category_id") private UUID categoryId;
    private String description; @Column(name = "transaction_date", nullable = false) private LocalDate transactionDate;
    @Column(nullable = false, precision = 19, scale = 4) private BigDecimal amount;
    @Column(name = "tax_amount", nullable = false, precision = 19, scale = 4) private BigDecimal taxAmount;
    @Column(nullable = false, length = 3) private String currency; @Column(name = "payment_status", nullable = false) private String paymentStatus;
    @Column(name = "expense_status", nullable = false) private String expenseStatus; @Column(name = "source_type", nullable = false) private String sourceType;
    @Column(name = "created_by", nullable = false) private UUID createdBy; @Column(name = "approved_by") private UUID approvedBy;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt; @Column(name = "updated_at", nullable = false) private Instant updatedAt;
    @PrePersist void create() { id = id == null ? UUID.randomUUID() : id; createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate void update() { updatedAt = Instant.now(); }
    public UUID getId() { return id; } public UUID getWorkspaceId() { return workspaceId; } public void setWorkspaceId(UUID workspaceId) { this.workspaceId = workspaceId; }
    public UUID getDocumentId() { return documentId; } public void setDocumentId(UUID documentId) { this.documentId = documentId; }
    public UUID getVendorId() { return vendorId; } public void setVendorId(UUID vendorId) { this.vendorId = vendorId; }
    public UUID getCategoryId() { return categoryId; } public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public LocalDate getTransactionDate() { return transactionDate; } public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getTaxAmount() { return taxAmount; } public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public String getCurrency() { return currency; } public void setCurrency(String currency) { this.currency = currency; }
    public String getPaymentStatus() { return paymentStatus; } public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getExpenseStatus() { return expenseStatus; } public void setExpenseStatus(String expenseStatus) { this.expenseStatus = expenseStatus; }
    public String getSourceType() { return sourceType; } public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public UUID getCreatedBy() { return createdBy; } public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
}
