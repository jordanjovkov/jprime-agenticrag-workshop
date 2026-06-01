package io.jprime.agenticrag.retriever.persistence.knowledgebase;

import org.springframework.ai.document.Document;

import java.util.List;

/**
 * Repository interface for performing vector similarity searches against the Knowledge Base.
 * <p>
 * The Knowledge Base contains chunked text from technical documentation of video editing cards,
 * stored as vector embeddings in pgvector. Implementations are responsible for translating
 * the search parameters into the appropriate vector store query.
 * <p>
 * Currently the only implementation is
 * {@link io.jprime.agenticrag.retriever.persistence.knowledgebase.impl.PgVectorKnowledgeBaseRepository}.
 */
public interface KnowledgeBaseRepository {

    /**
     * Searches the Knowledge Base for documents semantically similar to the given query.
     *
     * @param query               the search query text to embed and match against
     * @param topK                the maximum number of results to return
     * @param similarityThreshold the minimum similarity score in range [0.0, 1.0]
     * @return list of matching {@link Document} chunks ordered by similarity score descending
     */
    List<Document> search(String query, int topK, double similarityThreshold);
}
