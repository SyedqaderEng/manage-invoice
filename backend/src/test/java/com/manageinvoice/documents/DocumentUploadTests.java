package com.manageinvoice.documents;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DocumentUploadTests {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;

    private static final byte[] PNG = new byte[] {(byte)0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a};

    @Test
    void uploadsListsAndRejectsDuplicateDocument() throws Exception {
        String token = register("documents@example.com");
        JsonNode workspace = createWorkspace(token);
        MockMultipartFile file = new MockMultipartFile("file", "receipt.png", "image/png", PNG);

        MvcResult upload = mockMvc.perform(multipart("/api/v1/documents")
                .file(file).header("Authorization", "Bearer " + token)
                .header("X-Workspace-Id", workspace.get("id").asText()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.originalFilename").value("receipt.png"))
            .andExpect(jsonPath("$.processingStatus").value("UPLOADED"))
            .andReturn();

        mockMvc.perform(get("/api/v1/documents")
                .header("Authorization", "Bearer " + token)
                .header("X-Workspace-Id", workspace.get("id").asText()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(mapper.readTree(upload.getResponse().getContentAsString()).get("id").asText()));

        mockMvc.perform(multipart("/api/v1/documents")
                .file(new MockMultipartFile("file", "copy.png", "image/png", PNG))
                .header("Authorization", "Bearer " + token)
                .header("X-Workspace-Id", workspace.get("id").asText()))
            .andExpect(status().isConflict());
    }

    private String register(String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(java.util.Map.of("email", email, "password", "SecurePass123", "firstName", "Document", "lastName", "User"))))
            .andExpect(status().isOk()).andReturn();
        return mapper.readTree(result.getResponse().getContentAsString()).get("accessToken").asText();
    }

    private JsonNode createWorkspace(String token) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/workspaces")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(java.util.Map.of("name", "Documents", "workspaceType", "BUSINESS"))))
            .andExpect(status().isOk()).andReturn();
        return mapper.readTree(result.getResponse().getContentAsString());
    }
}
