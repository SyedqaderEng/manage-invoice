package com.manageinvoice.expenses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.manageinvoice.tenancy.WorkspaceService;

@Service
public class ExpenseService {
    private final ExpenseRepository expenses; private final WorkspaceService workspaces;
    public ExpenseService(ExpenseRepository expenses, WorkspaceService workspaces) { this.expenses=expenses; this.workspaces=workspaces; }
    public Page<ExpenseTransaction> list(UUID workspaceId, UUID userId, Pageable pageable) { workspaces.requireMembership(workspaceId,userId); return expenses.findByWorkspaceId(workspaceId,pageable); }
    @Transactional public ExpenseTransaction create(UUID workspaceId, UUID userId, CreateRequest request) {
        workspaces.requireMembership(workspaceId,userId);
        if (request.amount().compareTo(BigDecimal.ZERO)<0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Amount cannot be negative");
        ExpenseTransaction expense=new ExpenseTransaction(); expense.setWorkspaceId(workspaceId); expense.setDocumentId(request.documentId()); expense.setVendorId(request.vendorId()); expense.setCategoryId(request.categoryId());
        expense.setDescription(request.description()); expense.setTransactionDate(request.transactionDate()); expense.setAmount(request.amount()); expense.setTaxAmount(request.taxAmount());
        expense.setCurrency(request.currency()); expense.setPaymentStatus("UNPAID"); expense.setExpenseStatus("APPROVED"); expense.setSourceType(request.documentId()==null?"MANUAL":"DOCUMENT"); expense.setCreatedBy(userId);
        return expenses.save(expense);
    }
    public ExpenseTransaction get(UUID workspaceId, UUID userId, UUID id){ workspaces.requireMembership(workspaceId,userId); return expenses.findByWorkspaceIdAndId(workspaceId,id).orElseThrow(); }
    @Transactional public ExpenseTransaction update(UUID workspaceId, UUID userId, UUID id, CreateRequest request){ ExpenseTransaction expense=get(workspaceId,userId,id); if(request.amount().compareTo(BigDecimal.ZERO)<0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Amount cannot be negative"); expense.setDocumentId(request.documentId()); expense.setVendorId(request.vendorId()); expense.setCategoryId(request.categoryId()); expense.setDescription(request.description()); expense.setTransactionDate(request.transactionDate()); expense.setAmount(request.amount()); expense.setTaxAmount(request.taxAmount()); expense.setCurrency(request.currency().toUpperCase()); return expenses.save(expense); }
    public void delete(UUID workspaceId, UUID userId, UUID id){ workspaces.requireMembership(workspaceId,userId); ExpenseTransaction expense=expenses.findByWorkspaceIdAndId(workspaceId,id).orElseThrow(); expenses.delete(expense); }
    public record CreateRequest(UUID documentId, UUID vendorId, UUID categoryId, String description, LocalDate transactionDate, BigDecimal amount, BigDecimal taxAmount, String currency) {}
}
