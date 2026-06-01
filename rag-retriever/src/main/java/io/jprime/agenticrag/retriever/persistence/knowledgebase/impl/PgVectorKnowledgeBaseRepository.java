package io.jprime.agenticrag.retriever.persistence.knowledgebase.impl;

import io.jprime.agenticrag.retriever.persistence.knowledgebase.KnowledgeBaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * pgvector implementation of {@link KnowledgeBaseRepository}.
 * Performs vector similarity search against the Knowledge Base stored in PostgreSQL
 * via the pgvector extension.
 * <p>
 * Parameters are validated explicitly before the search is executed — pgvector
 * produces unclear errors when given invalid values, so failing fast with a
 * descriptive message improves debuggability.
 */
@Repository
public class PgVectorKnowledgeBaseRepository implements KnowledgeBaseRepository {

    private static final Logger log = LoggerFactory.getLogger(PgVectorKnowledgeBaseRepository.class);

    /**
     * Minimum allowed value for {@code topK} — the number of most similar documents to return.
     * Must be at least 1; zero or negative values are rejected.
     */
    private static final int MIN_TOP_K = 1;

    /**
     * Valid range for {@code similarityThreshold} — the minimum cosine similarity score
     * a document must have to be included in the results.
     * Range: [0.0, 1.0] where 0.0 means no filtering and 1.0 means exact match only.
     */
    private static final double MIN_SIMILARITY_THRESHOLD = 0.0;
    private static final double MAX_SIMILARITY_THRESHOLD = 1.0;

    private final VectorStore vectorStore;

    public PgVectorKnowledgeBaseRepository(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
     * Searches the Knowledge Base for documents similar to the given query.
     *
     * @param query               the search query text to embed and match against
     * @param topK                the maximum number of results to return (must be &gt;= {@value MIN_TOP_K})
     * @param similarityThreshold the minimum similarity score in range [{@value MIN_SIMILARITY_THRESHOLD}, {@value MAX_SIMILARITY_THRESHOLD}]
     * @return list of matching {@link Document} chunks ordered by similarity score descending
     * @throws IllegalArgumentException if {@code topK} or {@code similarityThreshold} are out of range
     */
    @Override
    public List<Document> search(String query, int topK, double similarityThreshold) {
        if (topK < MIN_TOP_K) {
            throw new IllegalArgumentException("When searching in pgvector topK must be a positive number, but is given: " + topK);
        }

        if (similarityThreshold < MIN_SIMILARITY_THRESHOLD || similarityThreshold > MAX_SIMILARITY_THRESHOLD) {
            throw new IllegalArgumentException(
                    "When searching in pgvector similarityThreshold must be in range [0.0, 1.0], but is given: " + similarityThreshold);
        }

        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .similarityThreshold(similarityThreshold)
                .build();

        List<Document> foundDocuments = vectorStore.similaritySearch(searchRequest);

        log.info("[pgvector] Search query: '{}' — topK: {}, threshold: {}, found: {} document(s)",
                query, topK, similarityThreshold, foundDocuments.size());

        return foundDocuments;
    }
}
