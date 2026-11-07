package com.manageinvoice.documents;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.manageinvoice.documents.storage.ObjectStorage;
import com.manageinvoice.identity.UserRepository;
import com.manageinvoice.tenancy.Workspace;
import com.manageinvoice.tenancy.WorkspaceService;

@Service
public class DocumentService {
    private static final long MAX_FILE_SIZE = 25L * 1024 * 1024;
    private final DocumentRepository documents;
    private final WorkspaceService workspaces;
    private final UserRepository users;
    private final ObjectStorage storage;

    public DocumentService(DocumentRepository documents, WorkspaceService workspaces, UserRepository users, ObjectStorage storage) {
        this.documents = documents;
        this.workspaces = workspaces;
        this.users = users;
        this.storage = storage;
    }

    public Page<DocumentResponse> list(UUID workspaceId, UUID userId, Pageable pageable) {
        Workspace workspace = workspaces.requireMembership(workspaceId, userId);
        return documents.findByWorkspaceIdAndDeletedAtIsNull(workspace.getId(), pageable).map(DocumentResponse::new);
    }

    @Transactional
    public DocumentResponse upload(UUID workspaceId, UUID userId, MultipartFile file) {
        Workspace workspace = workspaces.requireMembership(workspaceId, userId);
        if (file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        if (file.getSize() > MAX_FILE_SIZE) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File exceeds 25 MB");

        try {
            byte[] content = file.getBytes();
            String mimeType = detectMimeType(content);
            String checksum = sha256(content);
            documents.findFirstByWorkspaceIdAndChecksumAndDeletedAtIsNull(workspaceId, checksum).ifPresent(existing -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate document already exists: " + existing.getId());
            });

            UUID documentId = UUID.randomUUID();
            String extension = extensionFor(mimeType);
            String storageKey = workspaceId + "/" + documentId + extension;
            storage.put(storageKey, mimeType, content.length, new ByteArrayInputStream(content));

            Document document = new Document();
            document.setWorkspace(workspace);
            document.setUploadedBy(users.findById(userId).orElseThrow());
            document.setOriginalFilename(sanitizeFilename(file.getOriginalFilename()));
            document.setStorageKey(storageKey);
            document.setMimeType(mimeType);
            document.setFileSize(content.length);
            document.setChecksum(checksum);
            documents.save(document);
            return new DocumentResponse(document);
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to validate uploaded file", exception);
        }
    }

    public Document requireDocument(UUID workspaceId, UUID userId, UUID documentId) {
        workspaces.requireMembership(workspaceId, userId);
        return documents.findByWorkspaceIdAndIdAndDeletedAtIsNull(workspaceId, documentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
    }

    public byte[] download(UUID workspaceId, UUID userId, UUID documentId) {
        Document document = requireDocument(workspaceId, userId, documentId);
        try (var stream = storage.open(document.getStorageKey())) {
            return stream.readAllBytes();
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to read document", exception);
        }
    }

    @Transactional
    public void delete(UUID workspaceId, UUID userId, UUID documentId) {
        Document document = requireDocument(workspaceId, userId, documentId);
        document.softDelete();
    }

    private String detectMimeType(byte[] bytes) {
        if (startsWith(bytes, "%PDF-".getBytes(StandardCharsets.US_ASCII))) return "application/pdf";
        if (bytes.length >= 8 && bytes[0] == (byte)0x89 && bytes[1] == 0x50 && bytes[2] == 0x4e && bytes[3] == 0x47) return "image/png";
        if (bytes.length >= 3 && bytes[0] == (byte)0xff && bytes[1] == (byte)0xd8 && bytes[2] == (byte)0xff) return "image/jpeg";
        if (bytes.length >= 12 && startsWith(bytes, "RIFF".getBytes(StandardCharsets.US_ASCII)) && bytes[8] == 'W' && bytes[9] == 'E' && bytes[10] == 'B' && bytes[11] == 'P') return "image/webp";
        throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Supported formats are PDF, JPG, PNG, and WEBP");
    }

    private boolean startsWith(byte[] value, byte[] prefix) {
        if (value.length < prefix.length) return false;
        for (int index = 0; index < prefix.length; index++) if (value[index] != prefix[index]) return false;
        return true;
    }

    private String extensionFor(String mimeType) {
        return switch (mimeType) { case "application/pdf" -> ".pdf"; case "image/png" -> ".png"; case "image/jpeg" -> ".jpg"; default -> ".webp"; };
    }

    private String sanitizeFilename(String filename) {
        String value = filename == null ? "document" : filename.replace("\\", "_").replace("/", "_").trim();
        return value.isBlank() ? "document" : value.substring(0, Math.min(value.length(), 255));
    }

    private String sha256(byte[] bytes) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
