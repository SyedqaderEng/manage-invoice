package com.manageinvoice.review;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manageinvoice.common.security.AuthenticatedUser;

@RestController
@RequestMapping("/api/v1/documents/{documentId}/review")
public class ReviewController {
    private final ReviewService reviews;

    public ReviewController(ReviewService reviews) {
        this.reviews = reviews;
    }

    @GetMapping
    public ReviewService.ReviewState state(@AuthenticationPrincipal AuthenticatedUser user,
                                           @RequestHeader("X-Workspace-Id") UUID workspaceId,
                                           @PathVariable UUID documentId) {
        return reviews.get(workspaceId, user.id(), documentId);
    }

    @PostMapping("/save")
    public ReviewService.ReviewState save(@AuthenticationPrincipal AuthenticatedUser user,
                                          @RequestHeader("X-Workspace-Id") UUID workspaceId,
                                          @PathVariable UUID documentId,
                                          @RequestBody ReviewRequest request) {
        return reviews.save(workspaceId, user.id(), documentId, request.correctedData(), request.notes());
    }

    @PostMapping("/approve")
    public ReviewService.ReviewState approve(@AuthenticationPrincipal AuthenticatedUser user,
                                             @RequestHeader("X-Workspace-Id") UUID workspaceId,
                                             @PathVariable UUID documentId,
                                             @RequestBody ReviewRequest request) {
        return reviews.approve(workspaceId, user.id(), documentId, request.correctedData(), request.notes());
    }

    @PostMapping("/reject")
    public ReviewService.ReviewState reject(@AuthenticationPrincipal AuthenticatedUser user,
                                            @RequestHeader("X-Workspace-Id") UUID workspaceId,
                                            @PathVariable UUID documentId,
                                            @RequestBody(required = false) RejectionRequest request) {
        return reviews.reject(workspaceId, user.id(), documentId, request == null ? null : request.notes());
    }

    public record ReviewRequest(Map<String, Object> correctedData, String notes) {}
    public record RejectionRequest(String notes) {}
}
