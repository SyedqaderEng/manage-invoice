package com.manageinvoice.tenancy;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manageinvoice.common.security.AuthenticatedUser;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/workspaces")
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping
    public List<WorkspaceService.WorkspaceResponse> list(@AuthenticationPrincipal AuthenticatedUser principal) {
        return workspaceService.list(principal.id());
    }

    @PostMapping
    public WorkspaceService.WorkspaceResponse create(@AuthenticationPrincipal AuthenticatedUser principal,
                                                     @Valid @RequestBody CreateWorkspaceRequest request) {
        return workspaceService.create(principal.id(), new WorkspaceService.CreateWorkspaceRequest(request.name(), request.workspaceType()));
    }

    @GetMapping("/{workspaceId}/members")
    public List<WorkspaceService.MemberResponse> members(@AuthenticationPrincipal AuthenticatedUser principal,
                                                         @PathVariable UUID workspaceId) {
        return workspaceService.members(workspaceId, principal.id());
    }

    public record CreateWorkspaceRequest(@NotBlank String name, @NotNull WorkspaceType workspaceType) {}
}
