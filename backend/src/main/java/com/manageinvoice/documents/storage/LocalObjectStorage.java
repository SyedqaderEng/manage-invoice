package com.manageinvoice.documents.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LocalObjectStorage implements ObjectStorage {
    private final Path root;

    public LocalObjectStorage(@Value("${storage.local.root:./storage}") String root) throws IOException {
        this.root = Path.of(root).toAbsolutePath().normalize();
        Files.createDirectories(this.root);
    }

    @Override
    public String put(String key, String contentType, long contentLength, InputStream content) {
        try {
            Path destination = securePath(key);
            Files.createDirectories(destination.getParent());
            Files.copy(content, destination, StandardCopyOption.REPLACE_EXISTING);
            return key;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to store document", exception);
        }
    }

    @Override
    public InputStream open(String key) {
        try {
            return Files.newInputStream(securePath(key));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read document", exception);
        }
    }

    @Override
    public void delete(String key) {
        try {
            Files.deleteIfExists(securePath(key));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to delete document", exception);
        }
    }

    private Path securePath(String key) {
        Path path = root.resolve(key).normalize();
        if (!path.startsWith(root)) throw new IllegalArgumentException("Invalid storage key");
        return path;
    }
}
