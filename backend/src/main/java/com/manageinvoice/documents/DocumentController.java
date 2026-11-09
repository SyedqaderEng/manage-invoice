package com.manageinvoice.documents;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.manageinvoice.common.security.AuthenticatedUser;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final DocumentService documents;

    public DocumentController(DocumentService documents) {
        this.documents = documents;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DocumentResponse upload(@AuthenticationPrincipal AuthenticatedUser user,
                                   @RequestHeader("X-Workspace-Id") UUID workspaceId,
                                   @RequestParam("file") MultipartFile file) {
        return documents.upload(workspaceId, user.id(), file);
    }

    @GetMapping
    public Page<DocumentResponse> list(@AuthenticationPrincipal AuthenticatedUser user,
                                       @RequestHeader("X-Workspace-Id") UUID workspaceId,
                                       @PageableDefault(size = 20) Pageable pageable) {
        return documents.list(workspaceId, user.id(), pageable);
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<byte[]> download(@AuthenticationPrincipal AuthenticatedUser user,
                                           @RequestHeader("X-Workspace-Id") UUID workspaceId,
                                           @PathVariable UUID documentId) {
        Document document = documents.requireDocument(workspaceId, user.id(), documentId);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(document.getOriginalFilename()).build().toString())
            .contentType(MediaType.parseMediaType(document.getMimeType()))
            .body(documents.download(workspaceId, user.id(), documentId));
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AuthenticatedUser user,
                                       @RequestHeader("X-Workspace-Id") UUID workspaceId,
                                       @PathVariable UUID documentId) {
        documents.delete(workspaceId, user.id(), documentId);
        return ResponseEntity.noContent().build();
    }
}
