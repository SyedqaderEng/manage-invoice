package com.manageinvoice.invoices;
import java.math.BigDecimal; import java.time.Instant; import java.time.LocalDate; import java.util.UUID;
import jakarta.persistence.*;
@Entity @Table(name="invoices")
public class Invoice {
    @Id private UUID id;
    @Column(name="workspace_id", nullable=false) private UUID workspaceId;
    @Column(name="document_id") private UUID documentId; @Column(name="vendor_id") private UUID vendorId;
    @Column(name="invoice_number", nullable=false) private String invoiceNumber;
    @Column(name="invoice_date") private LocalDate invoiceDate; @Column(name="due_date") private LocalDate dueDate;
    @Column(nullable=false, precision=19, scale=4) private BigDecimal subtotal;
    @Column(name="tax_amount", nullable=false, precision=19, scale=4) private BigDecimal taxAmount;
    @Column(name="total_amount", nullable=false, precision=19, scale=4) private BigDecimal totalAmount;
    @Column(name="amount_paid", nullable=false, precision=19, scale=4) private BigDecimal amountPaid = BigDecimal.ZERO;
    @Column(name="balance_due", nullable=false, precision=19, scale=4) private BigDecimal balanceDue;
    @Column(nullable=false, length=3) private String currency; @Column(nullable=false) private String status;
    @Column(name="created_at", nullable=false, updatable=false) private Instant createdAt;
    @Column(name="updated_at", nullable=false) private Instant updatedAt;
    @PrePersist void create(){ id=id==null?UUID.randomUUID():id; createdAt=Instant.now(); updatedAt=createdAt; }
    @PreUpdate void update(){ updatedAt=Instant.now(); }
    public UUID getId(){return id;} public UUID getWorkspaceId(){return workspaceId;} public void setWorkspaceId(UUID workspaceId){this.workspaceId=workspaceId;}
    public UUID getDocumentId(){return documentId;} public void setDocumentId(UUID documentId){this.documentId=documentId;}
    public UUID getVendorId(){return vendorId;} public void setVendorId(UUID vendorId){this.vendorId=vendorId;}
    public String getInvoiceNumber(){return invoiceNumber;} public void setInvoiceNumber(String invoiceNumber){this.invoiceNumber=invoiceNumber;}
    public LocalDate getInvoiceDate(){return invoiceDate;} public void setInvoiceDate(LocalDate invoiceDate){this.invoiceDate=invoiceDate;}
    public LocalDate getDueDate(){return dueDate;} public void setDueDate(LocalDate dueDate){this.dueDate=dueDate;}
    public BigDecimal getSubtotal(){return subtotal;} public void setSubtotal(BigDecimal subtotal){this.subtotal=subtotal;}
    public BigDecimal getTaxAmount(){return taxAmount;} public void setTaxAmount(BigDecimal taxAmount){this.taxAmount=taxAmount;}
    public BigDecimal getTotalAmount(){return totalAmount;} public void setTotalAmount(BigDecimal totalAmount){this.totalAmount=totalAmount;}
    public BigDecimal getAmountPaid(){return amountPaid;} public void setAmountPaid(BigDecimal amountPaid){this.amountPaid=amountPaid;}
    public BigDecimal getBalanceDue(){return balanceDue;} public void setBalanceDue(BigDecimal balanceDue){this.balanceDue=balanceDue;}
    public String getCurrency(){return currency;} public void setCurrency(String currency){this.currency=currency;}
    public String getStatus(){return status;} public void setStatus(String status){this.status=status;}
}
