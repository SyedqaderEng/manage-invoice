package com.manageinvoice.tenancy;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.manageinvoice.identity.User;
import com.manageinvoice.identity.UserRepository;

@Service
public class WorkspaceService {
    private final WorkspaceRepository workspaces;
    private final WorkspaceMemberRepository members;
    private final UserRepository users;

    public WorkspaceService(WorkspaceRepository workspaces, WorkspaceMemberRepository members, UserRepository users) {
        this.workspaces = workspaces;
        this.members = members;
        this.users = users;
    }

    public List<WorkspaceResponse> list(UUID userId) {
        return members.findByUserIdAndStatus(userId, WorkspaceStatus.ACTIVE).stream()
            .map(member -> new WorkspaceResponse(member.getWorkspace(), member.getRole()))
            .toList();
    }

    @Transactional
    public WorkspaceResponse create(UUID ownerId, CreateWorkspaceRequest request) {
        User owner = users.findById(ownerId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account not found"));
        Workspace workspace = new Workspace();
        workspace.setName(request.name().trim());
        workspace.setWorkspaceType(request.workspaceType());
        workspace.setOwner(owner);
        workspaces.save(workspace);

        WorkspaceMember member = new WorkspaceMember();
        member.setWorkspace(workspace);
        member.setUser(owner);
        member.setRole(WorkspaceRole.OWNER);
        members.save(member);
        return new WorkspaceResponse(workspace, WorkspaceRole.OWNER);
    }

    public Workspace requireMembership(UUID workspaceId, UUID userId) {
        return members.findByWorkspaceIdAndUserIdAndStatus(workspaceId, userId, WorkspaceStatus.ACTIVE)
            .map(WorkspaceMember::getWorkspace)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Workspace access denied"));
    }

    public List<MemberResponse> members(UUID workspaceId, UUID userId) {
        requireMembership(workspaceId, userId);
        return members.findByWorkspaceIdAndStatus(workspaceId, WorkspaceStatus.ACTIVE).stream()
            .map(MemberResponse::new)
            .toList();
    }

    public record CreateWorkspaceRequest(String name, WorkspaceType workspaceType) {}
    public record WorkspaceResponse(UUID id, String name, WorkspaceType workspaceType, String defaultCurrency, WorkspaceRole role) {
        WorkspaceResponse(Workspace workspace, WorkspaceRole role) {
            this(workspace.getId(), workspace.getName(), workspace.getWorkspaceType(), workspace.getDefaultCurrency(), role);
        }
    }
    public record MemberResponse(UUID id, UUID userId, String email, WorkspaceRole role) {
        MemberResponse(WorkspaceMember member) {
            this(member.getId(), member.getUser().getId(), member.getUser().getEmail(), member.getRole());
        }
    }
}
