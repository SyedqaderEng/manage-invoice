package com.manageinvoice.invoices;
import java.util.UUID;
import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal; import org.springframework.web.bind.annotation.*;
import com.manageinvoice.common.security.AuthenticatedUser;
@RestController @RequestMapping("/api/v1/invoices")
public class InvoiceController {
 private final InvoiceService service; public InvoiceController(InvoiceService service){this.service=service;}
 @GetMapping public Page<Invoice> list(@AuthenticationPrincipal AuthenticatedUser user,@RequestHeader("X-Workspace-Id") UUID workspaceId,Pageable pageable){return service.list(workspaceId,user.id(),pageable);}
 @GetMapping("/{invoiceId}") public Invoice get(@AuthenticationPrincipal AuthenticatedUser user,@RequestHeader("X-Workspace-Id") UUID workspaceId,@PathVariable UUID invoiceId){return service.get(workspaceId,user.id(),invoiceId);}
 @PutMapping("/{invoiceId}") public Invoice update(@AuthenticationPrincipal AuthenticatedUser user,@RequestHeader("X-Workspace-Id") UUID workspaceId,@PathVariable UUID invoiceId,@RequestBody InvoiceService.CreateRequest request){return service.update(workspaceId,user.id(),invoiceId,request);}
 @DeleteMapping("/{invoiceId}") public org.springframework.http.ResponseEntity<Void> delete(@AuthenticationPrincipal AuthenticatedUser user,@RequestHeader("X-Workspace-Id") UUID workspaceId,@PathVariable UUID invoiceId){service.delete(workspaceId,user.id(),invoiceId);return org.springframework.http.ResponseEntity.noContent().build();}
 @PostMapping public Invoice create(@AuthenticationPrincipal AuthenticatedUser user,@RequestHeader("X-Workspace-Id") UUID workspaceId,@RequestBody InvoiceService.CreateRequest request){return service.create(workspaceId,user.id(),request);}
 @PostMapping("/{invoiceId}/payments") public Payment addPayment(@AuthenticationPrincipal AuthenticatedUser user,@RequestHeader("X-Workspace-Id") UUID workspaceId,@PathVariable UUID invoiceId,@RequestBody InvoiceService.PaymentRequest request){return service.addPayment(workspaceId,user.id(),invoiceId,request);}
}
