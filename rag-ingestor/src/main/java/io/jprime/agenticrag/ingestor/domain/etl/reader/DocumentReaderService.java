package io.jprime.agenticrag.ingestor.domain.etl.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ETL Read step — loads raw documents from the file system into Spring AI {@link Document} objects.
 * <p>
 * Uses Apache Tika via {@link TikaDocumentReader} to extract text from PDF files.
 * Tika handles format detection and text extraction automatically, making this step
 * format-agnostic — any file type Tika supports can be added to the Knowledge Base
 * without code changes.
 * <p>
 * Each loaded document is enriched with provenance metadata before being passed
 * to the Transform step:
 * <ul>
 *   <li>{@code source_filename} — original file name</li>
 *   <li>{@code source_path} — full URI path to the file</li>
 *   <li>{@code processed_date} — timestamp of when the file was read</li>
 *   <li>{@code file_size} — file size in bytes</li>
 * </ul>
 * This metadata is preserved through chunking and stored alongside each vector in pgvector,
 * enabling traceability of retrieved chunks back to their source document.
 */
@Component
public class DocumentReaderService {

    private static final Logger log = LoggerFactory.getLogger(DocumentReaderService.class);

    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    /**
     * Reads all documents matching the given path pattern and returns them as a flat list.
     * Files that cannot be read are skipped with an error log — a single unreadable file
     * does not abort the entire ingestion.
     *
     * @param documentsPath Ant-style path pattern (e.g. {@code file:/path/to/docs/*.pdf})
     * @return list of loaded {@link Document} objects with provenance metadata attached
     * @throws RuntimeException if the path pattern itself cannot be resolved
     */
    public List<Document> readAllDocuments(String documentsPath) {
        List<Document> loadedDocuments = new ArrayList<>();

        try {
            Resource[] resources = resolver.getResources(documentsPath);
            log.info("Found {} resources in knowledge base", resources.length);

            for (Resource resource : resources) {
                String resourceFilename = resource.getFilename();

                if (resource.isReadable() && resourceFilename != null && !resourceFilename.isEmpty()) {
                    try {
                        TikaDocumentReader reader = new TikaDocumentReader(resource);
                        List<Document> resourceDocuments = reader.get();

                        String fileSize = "unknown";
                        try {
                            fileSize = String.valueOf(resource.contentLength());
                        } catch (IOException e) {
                            log.warn("Could not determine file size for: {}", resourceFilename);
                        }

                        for (Document resourceDocument : resourceDocuments) {
                            resourceDocument.getMetadata().put("source_filename", resourceFilename);
                            resourceDocument.getMetadata().put("source_path", resource.getURI().toString());
                            resourceDocument.getMetadata().put("processed_date", LocalDateTime.now().toString());
                            resourceDocument.getMetadata().put("file_size", fileSize);
                        }

                        loadedDocuments.addAll(resourceDocuments);

                        log.info("Successfully read {} documents from file: {}", resourceDocuments.size(), resourceFilename);
                    } catch (Exception e) {
                        log.error("Failed to read file: {} - {}", resourceFilename, e.getMessage(), e);
                    }
                }
            }
            log.info("Loaded: {} documents", loadedDocuments.size());
        } catch (IOException e) {
            log.error("Failed to read documents from knowledge base", e);
            throw new RuntimeException("Failed to read documents from knowledge base", e);
        }

        return loadedDocuments;
    }
}
