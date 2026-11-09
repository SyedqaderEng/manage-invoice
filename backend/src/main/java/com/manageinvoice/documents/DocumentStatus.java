package com.manageinvoice.documents;

public enum DocumentStatus {
    UPLOADED,
    QUEUE,
    PROCESSING,
    EXTRACTED,
    REVIEW_REQUIRED,
    APPROVED,
    FAILED,
    ARCHIVED
}
