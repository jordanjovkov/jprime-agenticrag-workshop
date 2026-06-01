package io.jprime.agenticrag.retriever.domain.llm.factory;

import io.jprime.agenticrag.retriever.domain.llm.service.PromptService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Factory for creating {@link QuestionAnswerAdvisor} instances used in Naive RAG queries.
 * <p>
 * {@link QuestionAnswerAdvisor} intercepts the chat request, performs a vector similarity
 * search against the Knowledge Base, and injects the retrieved chunks into the prompt
 * before the LLM call — this is the core mechanism of Naive RAG.
 * <p>
 * The naive advisor is pre-built and cached at startup via {@link PostConstruct}
 * since its configuration (prompt template, topK, similarityThreshold) is static.
 */
@Component
public class RAGAdvisorFactory {

    private static final Logger log = LoggerFactory.getLogger(RAGAdvisorFactory.class);

    private final VectorStore vectorStore;
    private final PromptService promptService;
    private final Integer topK;
    private final Double similarityThreshold;

    private QuestionAnswerAdvisor cachedNaiveAdvisor;

    RAGAdvisorFactory(VectorStore vectorStore,
                      PromptService promptService,
                      @Value("${app.naiverag.searchrequest.topK}") Integer topK,
                      @Value("${app.naiverag.searchrequest.similarityThreshold}") Double similarityThreshold) {
        this.vectorStore = vectorStore;
        this.promptService = promptService;
        this.topK = topK;
        this.similarityThreshold = similarityThreshold;
    }

    /**
     * Builds and caches the Naive RAG advisor at application startup.
     * Logs the active retrieval parameters for observability during the workshop.
     */
    @PostConstruct
    void initAdvisors() {
        log.info("[RAGAdvisorFactory] Initializing Naive RAG advisor — topK: {}, similarityThreshold: {}",
                topK, similarityThreshold);

        PromptTemplate naiveRAGPromptTemplate = promptService.getNaiveRAGPromptTemplate();
        SearchRequest searchRequest = buildSearchRequest();

        cachedNaiveAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .promptTemplate(naiveRAGPromptTemplate)
                .searchRequest(searchRequest)
                .build();

        log.info("[RAGAdvisorFactory] Naive RAG advisor initialized successfully");
    }

    private SearchRequest buildSearchRequest() {
        return SearchRequest.builder()
                .topK(topK)
                .similarityThreshold(similarityThreshold)
                .build();
    }

    /**
     * Returns the cached {@link QuestionAnswerAdvisor} for Naive RAG.
     * The same instance is reused across all requests.
     */
    public QuestionAnswerAdvisor createNaiveAdvisor() {
        return cachedNaiveAdvisor;
    }

}
