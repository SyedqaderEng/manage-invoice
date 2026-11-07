package com.manageinvoice.invoices;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.manageinvoice.tenancy.WorkspaceService;

@Service
public class InvoiceService {
    private final InvoiceRepository invoices;
    private final PaymentRepository payments;
    private final WorkspaceService workspaces;

    public InvoiceService(InvoiceRepository invoices, PaymentRepository payments, WorkspaceService workspaces) {
        this.invoices=invoices; this.payments=payments; this.workspaces=workspaces;
    }

    public Page<Invoice> list(UUID workspaceId, UUID userId, Pageable pageable){workspaces.requireMembership(workspaceId,userId);return invoices.findByWorkspaceId(workspaceId,pageable);}

    @Transactional
    public Invoice create(UUID workspaceId, UUID userId, CreateRequest request){
        workspaces.requireMembership(workspaceId,userId);
        if(request.totalAmount().compareTo(BigDecimal.ZERO)<0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invoice total cannot be negative");
        BigDecimal tax=request.taxAmount()==null?BigDecimal.ZERO:request.taxAmount();
        Invoice invoice=new Invoice(); invoice.setWorkspaceId(workspaceId); invoice.setDocumentId(request.documentId()); invoice.setVendorId(request.vendorId());
        invoice.setInvoiceNumber(request.invoiceNumber().trim()); invoice.setInvoiceDate(request.invoiceDate()); invoice.setDueDate(request.dueDate());
        invoice.setSubtotal(request.subtotal()); invoice.setTaxAmount(tax); invoice.setTotalAmount(request.totalAmount()); invoice.setAmountPaid(BigDecimal.ZERO);
        invoice.setBalanceDue(request.totalAmount()); invoice.setCurrency(request.currency().toUpperCase()); invoice.setStatus("RECEIVED");
        return invoices.save(invoice);
    }

    @Transactional
    public Payment addPayment(UUID workspaceId, UUID userId, UUID invoiceId, PaymentRequest request){
        workspaces.requireMembership(workspaceId,userId);
        Invoice invoice=invoices.findByWorkspaceIdAndId(workspaceId,invoiceId).orElseThrow();
        if(!invoice.getCurrency().equalsIgnoreCase(request.currency())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Payment currency must match invoice currency");
        if(request.amount().compareTo(BigDecimal.ZERO)<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Payment amount must be positive");
        BigDecimal alreadyPaid=payments.findByWorkspaceIdAndInvoiceId(workspaceId,invoiceId).stream().map(Payment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal newPaid=alreadyPaid.add(request.amount());
        if(newPaid.compareTo(invoice.getTotalAmount())>0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Payment total cannot exceed invoice total");
        Payment payment=new Payment(); payment.setWorkspaceId(workspaceId); payment.setInvoiceId(invoiceId); payment.setPaymentDate(request.paymentDate()); payment.setAmount(request.amount()); payment.setCurrency(request.currency().toUpperCase()); payment.setPaymentMethod(request.paymentMethod()); payment.setReferenceNumber(request.referenceNumber()); payments.save(payment);
        recalculate(invoice,newPaid); return payment;
    }

    private void recalculate(Invoice invoice, BigDecimal paid){
        invoice.setAmountPaid(paid); invoice.setBalanceDue(invoice.getTotalAmount().subtract(paid));
        if(paid.compareTo(BigDecimal.ZERO)==0) invoice.setStatus("RECEIVED"); else if(paid.compareTo(invoice.getTotalAmount())>=0) invoice.setStatus("PAID"); else invoice.setStatus("PARTIALLY_PAID");
        invoices.save(invoice);
    }

    public record CreateRequest(UUID documentId,UUID vendorId,String invoiceNumber,LocalDate invoiceDate,LocalDate dueDate,BigDecimal subtotal,BigDecimal taxAmount,BigDecimal totalAmount,String currency){}
    public record PaymentRequest(LocalDate paymentDate,BigDecimal amount,String currency,String paymentMethod,String referenceNumber){}
}
