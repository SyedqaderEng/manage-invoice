package com.manageinvoice.processing.provider;

public interface DocumentExtractionProvider {
    String providerName();
    ExtractedDocument extract(String ocrText);

    record ExtractedDocument(String documentType, String vendorName, String documentNumber,
                             String documentDate, String dueDate, String currency,
                             String subtotal, String tax, String total, double confidence,
                             String rawJson) {}
}
