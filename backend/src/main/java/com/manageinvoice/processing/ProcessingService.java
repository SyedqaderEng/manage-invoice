package com.manageinvoice.processing;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manageinvoice.documents.Document;
import com.manageinvoice.documents.DocumentRepository;
import com.manageinvoice.documents.DocumentStatus;
import com.manageinvoice.documents.storage.ObjectStorage;
import com.manageinvoice.extraction.DocumentExtraction;
import com.manageinvoice.extraction.DocumentExtractionRepository;
import com.manageinvoice.processing.provider.DocumentExtractionProvider;
import com.manageinvoice.processing.provider.DocumentOcrProvider;
import com.manageinvoice.tenancy.WorkspaceService;

@Service
public class ProcessingService {
    private final ProcessingJobRepository jobs;
    private final DocumentRepository documents;
    private final DocumentExtractionRepository extractions;
    private final WorkspaceService workspaces;
    private final ObjectStorage storage;
    private final DocumentOcrProvider ocrProvider;
    private final DocumentExtractionProvider extractionProvider;
    private final ObjectMapper objectMapper;
    private final ApplicationContext applicationContext;

    public ProcessingService(ProcessingJobRepository jobs, DocumentRepository documents,
                             DocumentExtractionRepository extractions, WorkspaceService workspaces,
                             ObjectStorage storage, DocumentOcrProvider ocrProvider,
                             DocumentExtractionProvider extractionProvider, ObjectMapper objectMapper,
                             ApplicationContext applicationContext) {
        this.jobs = jobs;
        this.documents = documents;
        this.extractions = extractions;
        this.workspaces = workspaces;
        this.storage = storage;
        this.ocrProvider = ocrProvider;
        this.extractionProvider = extractionProvider;
        this.objectMapper = objectMapper;
        this.applicationContext = applicationContext;
    }

    @Transactional
    public ProcessingJob enqueue(UUID workspaceId, UUID documentId) {
        ProcessingJob job = new ProcessingJob();
        job.setWorkspaceId(workspaceId);
        job.setDocumentId(documentId);
        job.setJobType("OCR_AND_EXTRACT");
        job.setStatus(ProcessingJobStatus.QUEUE);
        jobs.save(job);
        applicationContext.getBean(ProcessingService.class).process(job.getId());
        return job;
    }

    @Async
    public void process(UUID jobId) {
        jobs.findById(jobId).ifPresent(this::processJob);
    }

    @Transactional
    protected void processJob(ProcessingJob job) {
        job.incrementAttempt();
        job.setStatus(ProcessingJobStatus.PROCESSING);
        Document document = documents.findById(job.getDocumentId()).orElseThrow();
        documentProcessingStatus(document, DocumentStatus.PROCESSING);
        try (var stream = storage.open(document.getStorageKey())) {
            DocumentOcrProvider.OcrResult ocr = ocrProvider.recognize(
                new DocumentOcrProvider.DocumentSource(stream.readAllBytes(), document.getMimeType(), document.getOriginalFilename()));
            DocumentExtractionProvider.ExtractedDocument extracted = extractionProvider.extract(ocr.text());
            Map<String, Object> normalized = new LinkedHashMap<>();
            normalized.put("documentType", extracted.documentType());
            normalized.put("vendorName", extracted.vendorName());
            normalized.put("documentNumber", extracted.documentNumber());
            normalized.put("documentDate", extracted.documentDate());
            normalized.put("dueDate", extracted.dueDate());
            normalized.put("currency", extracted.currency());
            normalized.put("subtotal", extracted.subtotal());
            normalized.put("tax", extracted.tax());
            normalized.put("total", extracted.total());
            normalized.put("overallConfidence", extracted.confidence());

            DocumentExtraction extraction = new DocumentExtraction();
            extraction.setWorkspaceId(job.getWorkspaceId());
            extraction.setDocumentId(job.getDocumentId());
            extraction.setExtractionVersion(nextVersion(job.getWorkspaceId(), job.getDocumentId()));
            extraction.setRawExtractionJson(extracted.rawJson());
            extraction.setNormalizedDataJson(objectMapper.writeValueAsString(normalized));
            extraction.setOverallConfidence(BigDecimal.valueOf(extracted.confidence()));
            extraction.setModelProvider(extractionProvider.providerName());
            extraction.setModelName(ocrProvider.providerName());
            extractions.save(extraction);

            job.complete();
            documentProcessingStatus(document, extracted.confidence() < 0.80 ? DocumentStatus.REVIEW_REQUIRED : DocumentStatus.EXTRACTED);
        } catch (Exception exception) {
            job.fail("PROCESSING_FAILED", exception.getMessage());
            documentProcessingStatus(document, DocumentStatus.FAILED);
        }
    }

    private int nextVersion(UUID workspaceId, UUID documentId) {
        return extractions.findFirstByWorkspaceIdAndDocumentIdOrderByExtractionVersionDesc(workspaceId, documentId)
            .map(extraction -> extraction.getExtractionVersion() + 1).orElse(1);
    }

    private void documentProcessingStatus(Document document, DocumentStatus status) {
        document.setProcessingStatus(status);
        documents.save(document);
    }

    public ProcessingJob enqueueAuthorized(UUID workspaceId, UUID userId, UUID documentId) {
        workspaces.requireMembership(workspaceId, userId);
        documents.findByWorkspaceIdAndIdAndDeletedAtIsNull(workspaceId, documentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
        return enqueue(workspaceId, documentId);
    }

    public ProcessingStatusResponse status(UUID workspaceId, UUID userId, UUID documentId) {
        workspaces.requireMembership(workspaceId, userId);
        Document document = documents.findByWorkspaceIdAndIdAndDeletedAtIsNull(workspaceId, documentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
        ProcessingJob job = jobs.findFirstByDocumentIdOrderByCreatedAtDesc(documentId).orElse(null);
        return new ProcessingStatusResponse(document.getId(), document.getProcessingStatus(),
            job == null ? null : job.getStatus(), job == null ? 0 : job.getAttemptCount(),
            job == null ? null : job.getErrorCode());
    }

    public Map<String, Object> extraction(UUID workspaceId, UUID userId, UUID documentId) {
        workspaces.requireMembership(workspaceId, userId);
        DocumentExtraction extraction = extractions.findFirstByWorkspaceIdAndDocumentIdOrderByExtractionVersionDesc(workspaceId, documentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Extraction not found"));
        try {
            return objectMapper.readValue(extraction.getNormalizedDataJson(), Map.class);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to read extraction", exception);
        }
    }

    public record ProcessingStatusResponse(UUID documentId, DocumentStatus documentStatus,
                                           ProcessingJobStatus jobStatus, int attemptCount, String errorCode) {}
}
