package com.manageinvoice.invoices;
import java.math.BigDecimal; import java.time.Instant; import java.time.LocalDate; import java.util.UUID;
import jakarta.persistence.*;
@Entity @Table(name="payments")
public class Payment {
    @Id private UUID id;
    @Column(name="workspace_id", nullable=false) private UUID workspaceId;
    @Column(name="invoice_id", nullable=false) private UUID invoiceId;
    @Column(name="payment_date", nullable=false) private LocalDate paymentDate;
    @Column(nullable=false, precision=19, scale=4) private BigDecimal amount;
    @Column(nullable=false, length=3) private String currency;
    @Column(name="payment_method") private String paymentMethod;
    @Column(name="reference_number") private String referenceNumber;
    @Column(nullable=false) private String status = "COMPLETED";
    @Column(name="created_at", nullable=false, updatable=false) private Instant createdAt;
    @PrePersist void create(){ id=id==null?UUID.randomUUID():id; createdAt=Instant.now(); }
    public UUID getId(){return id;} public UUID getWorkspaceId(){return workspaceId;} public void setWorkspaceId(UUID workspaceId){this.workspaceId=workspaceId;}
    public UUID getInvoiceId(){return invoiceId;} public void setInvoiceId(UUID invoiceId){this.invoiceId=invoiceId;}
    public LocalDate getPaymentDate(){return paymentDate;} public void setPaymentDate(LocalDate paymentDate){this.paymentDate=paymentDate;}
    public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal amount){this.amount=amount;}
    public String getCurrency(){return currency;} public void setCurrency(String currency){this.currency=currency;}
    public String getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(String paymentMethod){this.paymentMethod=paymentMethod;}
    public String getReferenceNumber(){return referenceNumber;} public void setReferenceNumber(String referenceNumber){this.referenceNumber=referenceNumber;}
}
