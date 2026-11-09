package com.manageinvoice.documents.storage;

import java.io.InputStream;

public interface ObjectStorage {
    String put(String key, String contentType, long contentLength, InputStream content);
    InputStream open(String key);
    void delete(String key);
}
