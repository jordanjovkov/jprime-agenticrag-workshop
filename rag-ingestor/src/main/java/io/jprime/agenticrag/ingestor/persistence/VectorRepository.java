package io.jprime.agenticrag.ingestor.persistence;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for all pgvector operations in the ingestion pipeline —
 * saving document chunks, clearing the store, and searching by similarity.
 * <p>
 * Embedding is handled transparently by the Spring AI {@link VectorStore} abstraction:
 * documents passed to {@link #save} are embedded automatically before being stored.
 * Similarly, the search query is embedded at query time before the similarity search.
 * <p>
 * The {@link EmbeddingModel} is injected to allow logging the active model class,
 * which is useful during the workshop when switching between OpenAI and Ollama embeddings.
 */
@Repository
public class VectorRepository {

    private static final Logger log = LoggerFactory.getLogger(VectorRepository.class);

    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;
    private final EmbeddingModel embeddingModel;

    @Value("${spring.ai.vectorstore.pgvector.table-name:vector_store}")
    private String vectorStoreTableName;

    @Value("${app.knowledgebase.search.topk:5}")
    private int topK;

    @Value("${app.knowledgebase.search.similarity-threshold:0.4}")
    private double similarityThreshold;

    public VectorRepository(VectorStore vectorStore, JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        this.vectorStore = vectorStore;
        this.jdbcTemplate = jdbcTemplate;
        this.embeddingModel = embeddingModel;
    }

    /**
     * Validates the configured table name at startup.
     * Prevents SQL injection in {@link #clearStore()} where the table name
     * is interpolated directly into a DELETE statement via {@link JdbcTemplate}.
     */
    @PostConstruct
    void validateTableName() {
        if (!vectorStoreTableName.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalStateException(
                    "Invalid vector store table name: '" + vectorStoreTableName + "'. " +
                            "Only alphanumeric characters and underscores are allowed.");
        }
    }

    /**
     * Embeds and saves the given document chunks to pgvector.
     * Throws a {@link RuntimeException} on failure to signal the ETL pipeline to abort —
     * a partial write would leave the Knowledge Base in an inconsistent state.
     *
     * @param documents the chunked documents to embed and store
     */
    public void save(List<Document> documents) {
        try {
            vectorStore.accept(documents);
            log.info("Saved {} document chunks to the RAG Vector Store", documents.size());
        } catch (Exception e) {
            log.error("Failed to save {} document chunks to the RAG Vector Store. " +
                    "Possible causes: network error, embedding model quota exceeded, or pgvector unavailable.",
                    documents.size(), e);
            throw new RuntimeException("Failed to save documents to the RAG Vector Store", e);
        }
    }

    /**
     * Deletes all document chunks from the vector store table.
     * Called during Knowledge Base reload after a successful Read and Transform phase.
     */
    public void clearStore() {
        jdbcTemplate.execute("DELETE FROM " + vectorStoreTableName);
        log.info("VectorStore <{}> cleared!", vectorStoreTableName);
    }

    /**
     * Searches the vector store for document chunks similar to the given query.
     * Search parameters ({@code topK}, {@code similarityThreshold}) are configured
     * via {@code app.knowledgebase.search.*} properties.
     *
     * @param query the search query to embed and match against
     * @return list of matching {@link Document} chunks ordered by similarity score descending
     */
    public List<Document> search(String query) {
        log.debug("Embedding model class: {}", embeddingModel.getClass().getName());

        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .similarityThreshold(similarityThreshold)
                .build();

        List<Document> documents = vectorStore.similaritySearch(searchRequest);

        log.info("Searching with query: <{}>", query);
        log.info("Found results: {}", documents.size());

        return documents;
    }
}
