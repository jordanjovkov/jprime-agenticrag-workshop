package io.jprime.agenticrag.ingestor.domain.service;

import io.jprime.agenticrag.ingestor.domain.etl.reader.DocumentReaderService;
import io.jprime.agenticrag.ingestor.domain.etl.transformer.DocumentTransformerService;
import io.jprime.agenticrag.ingestor.domain.etl.writer.DocumentWriterService;
import io.jprime.agenticrag.ingestor.persistence.VectorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Orchestrates the RAG Knowledge Base lifecycle — ingestion and search.
 * <p>
 * Ingestion follows a standard ETL pipeline:
 * <ol>
 *   <li><b>Read</b> — load raw documents from the file system via {@link DocumentReaderService}</li>
 *   <li><b>Transform</b> — split and enrich documents via {@link DocumentTransformerService}</li>
 *   <li><b>Write</b> — embed and store chunks in pgvector via {@link DocumentWriterService}</li>
 * </ol>
 * The Knowledge Base is cleared only after successful Read and Transform steps,
 * preventing data loss if an earlier phase fails.
 */
@Service
public class KnowledgeBaseService {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseService.class);

    @Value("${app.knowledgebase.path}")
    private String knowledgeBasePath;

    private final DocumentReaderService documentReaderService;
    private final DocumentTransformerService documentTransformerService;
    private final DocumentWriterService documentWriterService;
    private final VectorRepository vectorRepository;

    public KnowledgeBaseService(DocumentReaderService documentReaderService,
                                DocumentTransformerService documentTransformerService,
                                DocumentWriterService documentWriterService,
                                VectorRepository vectorRepository) {
        this.documentReaderService = documentReaderService;
        this.documentTransformerService = documentTransformerService;
        this.documentWriterService = documentWriterService;
        this.vectorRepository = vectorRepository;
    }

    /**
     * Facade for the ETL pipeline for reloading the Knowledge Base.
     * <p>
     * NOTE: Non-atomic operation — Knowledge Base is unavailable during reload.
     * Acceptable for workshop use!
     * For production, consider a blue/green approach!
     * <p>
     * Performs the three ETL major steps, clearing the Knowledge Base only after
     * successful reading and transformation to prevent data loss on failure:
     * 1. Reading: Read all documents from the Knowledge Base
     * 2. Transforming: Transform documents
     * 3. Clearing: Clear the Knowledge Base only if steps 1 and 2 succeeded
     * 4. Writing: Write transformed documents to the vector database
     */
    public void reloadKnowledgeBase() {
        List<Document> readDocuments = loadDocuments();
        printReadDocumentsInfo(readDocuments);
        List<Document> transformedDocuments = transformDocuments(readDocuments);

        clearKnowledgeBase();

        log.info("Start writing data to RAG Knowledge Base");
        log.info("It can take from seconds to several minutes!");
        log.info("Please wait...");

        writeDocuments(transformedDocuments);
        log.info("RAG Knowledge Base reloaded successfully");
    }

    /**
     * Searches the Knowledge Base and returns a list of matching text fragments.
     */
    public List<String> search(String query) {
        List<Document> foundSimilarDocuments = vectorRepository.search(query);
        return foundSimilarDocuments.stream()
                .map(Document::getText)
                .toList();
    }

    /**
     * Searches the Knowledge Base and returns the results as a formatted text.
     * Intended for use in the REST endpoint response.
     */
    public String searchAndFormatResults(String query) {
        List<String> foundMatchingTexts = search(query);

        StringBuilder searchResult = new StringBuilder();
        for (String text : foundMatchingTexts) {
            searchResult.append(text);
            searchResult.append("\n---\n");
        }
        return searchResult.toString();
    }

    private static void printReadDocumentsInfo(List<Document> readDocuments) {
        readDocuments.forEach(readDocument -> {
            String sourceFilename = Objects.toString(
                    readDocument.getMetadata().get("source_filename"), "unknown");

            String text = readDocument.getText();
            int textLength = 0;
            if (text != null) {
                textLength = text.length();
            }

            log.info("Document: {}, text length: {} chars", sourceFilename, textLength);
        });
    }

    private void clearKnowledgeBase() {
        vectorRepository.clearStore();
    }

    private void writeDocuments(List<Document> transformedDocuments) {
        documentWriterService.writeDocuments(transformedDocuments);
    }

    private List<Document> transformDocuments(List<Document> documents) {
        return documentTransformerService.transformDocuments(documents);
    }

    private List<Document> loadDocuments() {
        List<Document> loadedDocuments = documentReaderService.readAllDocuments(knowledgeBasePath);
        log.info("Loaded data from {} documents from the RAG Knowledge Base", loadedDocuments.size());
        return loadedDocuments;
    }
}
