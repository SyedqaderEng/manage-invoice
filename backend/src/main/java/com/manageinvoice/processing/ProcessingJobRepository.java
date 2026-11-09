package com.manageinvoice.processing;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessingJobRepository extends JpaRepository<ProcessingJob, UUID> {
    Optional<ProcessingJob> findFirstByDocumentIdOrderByCreatedAtDesc(UUID documentId);
    List<ProcessingJob> findByStatusOrderByCreatedAtAsc(ProcessingJobStatus status);
}
