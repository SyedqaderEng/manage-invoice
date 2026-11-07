package com.manageinvoice.expenses;
import java.util.UUID; import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable; import org.springframework.http.ResponseEntity; import org.springframework.security.core.annotation.AuthenticationPrincipal; import org.springframework.web.bind.annotation.*;
import com.manageinvoice.common.security.AuthenticatedUser;
@RestController @RequestMapping("/api/v1/expenses")
public class ExpenseController {
    private final ExpenseService service; public ExpenseController(ExpenseService service){this.service=service;}
    @GetMapping public Page<ExpenseTransaction> list(@AuthenticationPrincipal AuthenticatedUser user,@RequestHeader("X-Workspace-Id") UUID workspaceId,Pageable pageable){return service.list(workspaceId,user.id(),pageable);}
    @PostMapping public ExpenseTransaction create(@AuthenticationPrincipal AuthenticatedUser user,@RequestHeader("X-Workspace-Id") UUID workspaceId,@RequestBody ExpenseService.CreateRequest request){return service.create(workspaceId,user.id(),request);}
    @DeleteMapping("/{expenseId}") public ResponseEntity<Void> delete(@AuthenticationPrincipal AuthenticatedUser user,@RequestHeader("X-Workspace-Id") UUID workspaceId,@PathVariable UUID expenseId){service.delete(workspaceId,user.id(),expenseId);return ResponseEntity.noContent().build();}
}
