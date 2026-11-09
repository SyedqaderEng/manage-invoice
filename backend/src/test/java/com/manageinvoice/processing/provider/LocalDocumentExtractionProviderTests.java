package com.manageinvoice.processing.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class LocalDocumentExtractionProviderTests {
    private final LocalDocumentExtractionProvider provider = new LocalDocumentExtractionProvider(new ObjectMapper());

    @Test
    void extractsSupportedFieldsWithoutInventingValues() {
        DocumentExtractionProvider.ExtractedDocument result = provider.extract("Northstar Office\nInvoice #INV-1042\nDate: 2026-11-07\nTotal: $284.00");

        assertEquals("INVOICE", result.documentType());
        assertEquals("Northstar Office", result.vendorName());
        assertEquals("INV-1042", result.documentNumber());
        assertEquals("2026-11-07", result.documentDate());
        assertEquals("284.00", result.total());
        assertNotNull(result.rawJson());
    }
}
