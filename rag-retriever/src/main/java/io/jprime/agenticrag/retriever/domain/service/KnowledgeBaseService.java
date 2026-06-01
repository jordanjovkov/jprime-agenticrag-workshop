package io.jprime.agenticrag.retriever.domain.service;

import io.jprime.agenticrag.retriever.persistence.knowledgebase.KnowledgeBaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeBaseService {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseService.class);

    private final KnowledgeBaseRepository knowledgeBaseRepository;

    public KnowledgeBaseService(KnowledgeBaseRepository knowledgeBaseRepository) {
        this.knowledgeBaseRepository = knowledgeBaseRepository;
    }

    /**
     * Searches the Knowledge Base using explicitly provided topK and similarityThreshold.
     * Use this overload when specific search parameters are needed for a particular use case,
     * without affecting the global configuration.
     */
    public List<String> search(String query, int topK, double similarityThreshold) {
        log.info("[KnowledgeBase] Searching — query: '{}', topK: {}, similarityThreshold: {}",
                query, topK, similarityThreshold);

        List<Document> foundSimilarDocuments = knowledgeBaseRepository.search(query, topK, similarityThreshold);
        log.info("[KnowledgeBase] Found {} document(s)", foundSimilarDocuments.size());

        foundSimilarDocuments.forEach(doc -> {
            log.info("[KnowledgeBase] Found similar chunked document — metadata: {}", doc.getMetadata());
            log.info("[KnowledgeBase] Content preview: {}",
                    doc.getText().substring(0, Math.min(200, doc.getText().length())));
        });

        return foundSimilarDocuments.stream()
                .map(Document::getText)
                .toList();
    }
}
