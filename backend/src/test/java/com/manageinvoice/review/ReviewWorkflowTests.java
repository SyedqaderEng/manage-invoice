package com.manageinvoice.review;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manageinvoice.documents.Document;
import com.manageinvoice.documents.DocumentRepository;
import com.manageinvoice.documents.DocumentStatus;
import com.manageinvoice.extraction.DocumentExtraction;
import com.manageinvoice.extraction.DocumentExtractionRepository;
import com.manageinvoice.identity.User;
import com.manageinvoice.identity.UserRepository;
import com.manageinvoice.tenancy.Workspace;
import com.manageinvoice.tenancy.WorkspaceMember;
import com.manageinvoice.tenancy.WorkspaceMemberRepository;
import com.manageinvoice.tenancy.WorkspaceRepository;
import com.manageinvoice.tenancy.WorkspaceRole;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReviewWorkflowTests {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;
    @Autowired UserRepository users;
    @Autowired WorkspaceRepository workspaces;
    @Autowired WorkspaceMemberRepository members;
    @Autowired DocumentRepository documents;
    @Autowired DocumentExtractionRepository extractions;
    @Autowired DocumentReviewRepository reviews;

    @Test
    void savesApprovesAndAuditLogsReviewedDocument() throws Exception {
        String ownerToken = register("review-owner@example.com", "Owner", "User");
        UUID workspaceId = createWorkspace(ownerToken, "Review Workspace");
        UUID documentId = createDocument(workspaceId, "review-owner@example.com");

        Map<String, Object> corrected = Map.of("documentType", "INVOICE", "vendorName", "Corrected Vendor",
            "documentNumber", "INV-200", "total", "125.50", "currency", "USD");

        mockMvc.perform(post("/api/v1/documents/{id}/review/save", documentId)
                .header("Authorization", "Bearer " + ownerToken)
                .header("X-Workspace-Id", workspaceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Map.of("correctedData", corrected, "notes", "Draft correction"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.correctedData").exists());

        mockMvc.perform(post("/api/v1/documents/{id}/review/approve", documentId)
                .header("Authorization", "Bearer " + ownerToken)
                .header("X-Workspace-Id", workspaceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Map.of("correctedData", corrected, "notes", "Approved correction"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APPROVED"));

        org.junit.jupiter.api.Assertions.assertEquals(2, reviews.findByWorkspaceIdAndDocumentIdOrderByReviewedAtDesc(workspaceId, documentId).size());
    }

    @Test
    void viewerCannotApproveDocument() throws Exception {
        String ownerToken = register("viewer-owner@example.com", "Owner", "User");
        UUID workspaceId = createWorkspace(ownerToken, "Viewer Workspace");
        UUID documentId = createDocument(workspaceId, "viewer-owner@example.com");
        String viewerToken = register("review-viewer@example.com", "Review", "Viewer");

        Workspace workspace = workspaces.findById(workspaceId).orElseThrow();
        User viewer = users.findByEmailIgnoreCase("review-viewer@example.com").orElseThrow();
        WorkspaceMember membership = new WorkspaceMember();
        membership.setWorkspace(workspace);
        membership.setUser(viewer);
        membership.setRole(WorkspaceRole.VIEWER);
        members.save(membership);

        Map<String, Object> corrected = Map.of("total", "125.50", "currency", "USD");
        mockMvc.perform(post("/api/v1/documents/{id}/review/approve", documentId)
                .header("Authorization", "Bearer " + viewerToken)
                .header("X-Workspace-Id", workspaceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Map.of("correctedData", corrected))))
            .andExpect(status().isForbidden());
    }

    private String register(String email, String firstName, String lastName) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Map.of("email", email, "password", "SecurePass123", "firstName", firstName, "lastName", lastName))))
            .andExpect(status().isOk()).andReturn();
        return mapper.readTree(result.getResponse().getContentAsString()).get("accessToken").asText();
    }

    private UUID createWorkspace(String token, String name) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/workspaces")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Map.of("name", name, "workspaceType", "BUSINESS"))))
            .andExpect(status().isOk()).andReturn();
        return UUID.fromString(mapper.readTree(result.getResponse().getContentAsString()).get("id").asText());
    }

    private UUID createDocument(UUID workspaceId, String email) throws Exception {
        Workspace workspace = workspaces.findById(workspaceId).orElseThrow();
        User user = users.findByEmailIgnoreCase(email).orElseThrow();
        Document document = new Document();
        document.setWorkspace(workspace);
        document.setUploadedBy(user);
        document.setOriginalFilename("invoice.png");
        document.setStorageKey(workspaceId + "/review.png");
        document.setMimeType("image/png");
        document.setFileSize(8);
        document.setChecksum(UUID.randomUUID().toString().replace("-", ""));
        document.setProcessingStatus(DocumentStatus.REVIEW_REQUIRED);
        documents.save(document);

        DocumentExtraction extraction = new DocumentExtraction();
        extraction.setWorkspaceId(workspaceId);
        extraction.setDocumentId(document.getId());
        extraction.setExtractionVersion(1);
        extraction.setRawExtractionJson("{}");
        extraction.setNormalizedDataJson("{\"documentType\":\"INVOICE\",\"vendorName\":\"Original Vendor\",\"total\":\"100.00\",\"currency\":\"USD\"}");
        extraction.setOverallConfidence(BigDecimal.valueOf(0.65));
        extraction.setModelProvider("test");
        extraction.setModelName("test");
        extractions.save(extraction);
        return document.getId();
    }
}
