package com.manageinvoice.processing;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manageinvoice.common.security.AuthenticatedUser;

@RestController
@RequestMapping("/api/v1/documents/{documentId}")
public class ProcessingController {
    private final ProcessingService processingService;

    public ProcessingController(ProcessingService processingService) {
        this.processingService = processingService;
    }

    @GetMapping("/processing-status")
    public ProcessingService.ProcessingStatusResponse status(@AuthenticationPrincipal AuthenticatedUser user,
                                                              @RequestHeader("X-Workspace-Id") UUID workspaceId,
                                                              @PathVariable UUID documentId) {
        return processingService.status(workspaceId, user.id(), documentId);
    }

    @GetMapping("/extraction")
    public Map<String, Object> extraction(@AuthenticationPrincipal AuthenticatedUser user,
                                          @RequestHeader("X-Workspace-Id") UUID workspaceId,
                                          @PathVariable UUID documentId) {
        return processingService.extraction(workspaceId, user.id(), documentId);
    }

    @PostMapping("/reprocess")
    public Map<String, Boolean> reprocess(@AuthenticationPrincipal AuthenticatedUser user,
                                          @RequestHeader("X-Workspace-Id") UUID workspaceId,
                                          @PathVariable UUID documentId) {
        processingService.enqueueAuthorized(workspaceId, user.id(), documentId);
        return Map.of("queued", true);
    }
}
