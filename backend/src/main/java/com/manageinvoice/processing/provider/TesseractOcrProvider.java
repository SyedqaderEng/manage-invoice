package com.manageinvoice.processing.provider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "processing.ocr.provider", havingValue = "tesseract", matchIfMissing = true)
public class TesseractOcrProvider implements DocumentOcrProvider {
    @Override
    public String providerName() { return "tesseract"; }

    @Override
    public OcrResult recognize(DocumentSource document) {
        try {
            Path source = Files.createTempFile("manage-invoice-", extension(document.mimeType()));
            Files.write(source, document.content());
            ProcessBuilder builder;
            if ("application/pdf".equals(document.mimeType())) {
                Path outputDir = Files.createTempDirectory("manage-invoice-pdf-");
                run(List.of("pdftoppm", "-png", source.toString(), outputDir.resolve("page").toString()));
                StringBuilder text = new StringBuilder();
                List<Path> pages = Files.list(outputDir).filter(path -> path.toString().endsWith(".png")).sorted().toList();
                for (Path page : pages) text.append(runOutput(List.of("tesseract", page.toString(), "stdout", "--psm", "6"))).append('\n');
                return new OcrResult(text.toString().trim(), pages.size());
            }
            String text = runOutput(List.of("tesseract", source.toString(), "stdout", "--psm", "6"));
            Files.deleteIfExists(source);
            return new OcrResult(text.trim(), 1);
        } catch (IOException | InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("OCR provider failed", exception);
        }
    }

    private String extension(String mimeType) {
        return switch (mimeType) {
            case "application/pdf" -> ".pdf";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };
    }

    private void run(List<String> command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
        process.getInputStream().transferTo(java.io.OutputStream.nullOutputStream());
        if (process.waitFor() != 0) throw new IOException("OCR command failed: " + String.join(" ", command));
    }

    private String runOutput(List<String> command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
        String output = new String(process.getInputStream().readAllBytes());
        if (process.waitFor() != 0) throw new IOException("OCR command failed: " + String.join(" ", command));
        return output;
    }
}
