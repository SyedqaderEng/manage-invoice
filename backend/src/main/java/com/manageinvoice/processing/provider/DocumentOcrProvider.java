package com.manageinvoice.processing.provider;

public interface DocumentOcrProvider {
    String providerName();
    OcrResult recognize(DocumentSource document);

    record DocumentSource(byte[] content, String mimeType, String filename) {}
    record OcrResult(String text, int pageCount) {}
}
