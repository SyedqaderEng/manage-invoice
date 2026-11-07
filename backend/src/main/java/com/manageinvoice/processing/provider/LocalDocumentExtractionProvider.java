package com.manageinvoice.processing.provider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class LocalDocumentExtractionProvider implements DocumentExtractionProvider {
    private final ObjectMapper objectMapper;

    public LocalDocumentExtractionProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String providerName() { return "local-rule-extractor"; }

    @Override
    public ExtractedDocument extract(String ocrText) {
        String vendor = firstLine(ocrText);
        String documentNumber = find(ocrText, "(?:invoice|receipt|document)\\s*#?\\s*([A-Z]+-[A-Z0-9-]+|[A-Z0-9-]{3,})", 1);
        String date = find(ocrText, "(?:date)[^\\d]*(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2}|\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4})", 1);
        String total = find(ocrText, "(?:total|amount due|balance)[^\\d$]*(?:\\$)?\\s*(\\d+(?:\\.\\d{2})?)", 1);
        double confidence = total == null ? 0.35 : 0.65;
        try {
            String json = objectMapper.writeValueAsString(java.util.Map.of("textLength", ocrText.length(), "documentType", classify(ocrText)));
            return new ExtractedDocument(classify(ocrText), vendor, documentNumber, date, null, "USD", null, null, total, confidence, json);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to serialize extraction", exception);
        }
    }

    private String classify(String text) {
        String lower = text.toLowerCase();
        if (lower.contains("invoice")) return "INVOICE";
        if (lower.contains("receipt") || lower.contains("payment received")) return "RECEIPT";
        return "UNKNOWN";
    }

    private String firstLine(String text) {
        return text.lines().map(String::trim).filter(line -> !line.isBlank()).findFirst().orElse(null);
    }

    private String find(String text, String expression, int group) {
        Matcher matcher = Pattern.compile(expression, Pattern.CASE_INSENSITIVE).matcher(text);
        return matcher.find() ? matcher.group(group).trim() : null;
    }
}
