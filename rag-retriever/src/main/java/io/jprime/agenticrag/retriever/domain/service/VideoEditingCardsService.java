package io.jprime.agenticrag.retriever.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jprime.agenticrag.retriever.domain.llm.factory.RAGAdvisorFactory;
import io.jprime.agenticrag.retriever.domain.llm.service.PromptService;
import io.jprime.agenticrag.retriever.domain.model.constant.VideoEditingCardNames;
import io.jprime.agenticrag.retriever.domain.model.llmresponse.VideoEditingCardList;
import io.jprime.agenticrag.retriever.domain.observability.LoggingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service responsible for retrieving and assembling a structured list of video editing cards
 * using Naive RAG. Demonstrates two different retrieval strategies and their trade-offs.
 * <p>
 * Used in the Naive RAG step of the workshop to illustrate a key limitation:
 * when a single prompt is used as the pgvector search query, embedding similarity bias
 * causes only chunks from one document to be retrieved — leaving other products unrepresented.
 */
@Service
public class VideoEditingCardsService {

    private static final Logger log = LoggerFactory.getLogger(VideoEditingCardsService.class);

    private static final Pattern JSON_PATTERN = Pattern.compile("\\{[\\s\\S]*\\}", Pattern.DOTALL);

    private final ChatClient chatClient;
    private final RAGAdvisorFactory advisorFactory;
    private final PromptService promptService;
    private final KnowledgeBaseService knowledgeBaseService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.naiverag.videoeditingcards.multidocument.searchrequest.topK}")
    private int multiDocumentTopK;

    @Value("${app.naiverag.videoeditingcards.multidocument.searchrequest.similarityThreshold}")
    private double multiDocumentSimilarityThreshold;

    public VideoEditingCardsService(@Qualifier("naiveChatClient") ChatClient chatClient,
                                    RAGAdvisorFactory advisorFactory,
                                    PromptService promptService,
                                    KnowledgeBaseService knowledgeBaseService) {
        this.chatClient = chatClient;
        this.advisorFactory = advisorFactory;
        this.promptService = promptService;
        this.knowledgeBaseService = knowledgeBaseService;
    }

    /**
     * Naive RAG approach — demonstrates the single-document limitation.
     * <p>
     * The full prompt is used as the pgvector search query via {@link QuestionAnswerAdvisor},
     * which results in retrieving chunks from only one document due to embedding similarity bias.
     * Cards from other documents are likely to be missing or hallucinated in the response.
     */
    public VideoEditingCardList getVideoEditingCardList() {
        log.info("[VideoEditingCardsService] getVideoEditingCardList called");

        String prompt = promptService.getVideoEditingCardsPrompt();
        log.info("[VideoEditingCardsService] prompt ({} chars): '{}'",
                prompt.length(), LoggingUtils.truncate(prompt));

        QuestionAnswerAdvisor advisor = advisorFactory.createNaiveAdvisor();

        String rawContent = chatClient
                .prompt(prompt)
                .advisors(advisor)
                .call()
                .content();

        log.info("[VideoEditingCardsService] raw LLM response ({} chars): '{}'",
                rawContent != null ? rawContent.length() : 0,
                rawContent != null ? rawContent : "");

        return parseJsonResponse(rawContent);
    }

    /**
     * Multi-document RAG approach — solves the single-document limitation.
     * <p>
     * Performs a targeted Knowledge Base search per video editing card name,
     * combines all retrieved chunks into a single context, and passes it
     * directly to the LLM — bypassing {@link QuestionAnswerAdvisor} entirely.
     * This ensures all documents contribute to the result regardless of
     * embedding similarity bias.
     */
    public VideoEditingCardList getVideoEditingCardListMultiDocument() {
        log.info("[VideoEditingCardsService] getVideoEditingCardListMultiDocument called");
        log.info("[VideoEditingCardsService] search parameters — topK: {}, similarityThreshold: {}",
                multiDocumentTopK, multiDocumentSimilarityThreshold);

        List<String> allChunks = new ArrayList<>();

        for (String cardName : VideoEditingCardNames.getAll()) {
            String searchQuery = cardName + " hardware specifications";
            log.info("[VideoEditingCardsService] searching Knowledge Base for: '{}'", searchQuery);

            List<String> chunks = knowledgeBaseService.search(searchQuery, multiDocumentTopK, multiDocumentSimilarityThreshold);
            log.info("[VideoEditingCardsService] found {} chunk(s) for '{}'", chunks.size(), cardName);

            allChunks.addAll(chunks);
        }

        log.info("[VideoEditingCardsService] total chunks collected from all documents: {}", allChunks.size());

        String combinedContext = String.join("\n---\n", allChunks);
        String promptTemplate = promptService.getVideoEditingCardsMultiDocumentPrompt();
        String prompt = promptTemplate.replace("{context}", combinedContext);

        log.info("[VideoEditingCardsService] final prompt ({} chars)", prompt.length());

        String rawContent = chatClient
                .prompt(prompt)
                .call()
                .content();

        log.info("[VideoEditingCardsService] raw LLM response ({} chars): '{}'",
                rawContent != null ? rawContent.length() : 0,
                LoggingUtils.truncate(rawContent != null ? rawContent : ""));

        return parseJsonResponse(rawContent);
    }

    /**
     * Extracts and deserializes a {@link VideoEditingCardList} from a raw LLM response string.
     * Uses a regex to locate the JSON object within the response, tolerating extra text
     * or markdown formatting that the LLM may include around the JSON payload.
     */
    private VideoEditingCardList parseJsonResponse(String rawContent) {
        if (rawContent == null || rawContent.isBlank()) {
            log.warn("[VideoEditingCardsService] empty response from LLM — returning empty list");
            return new VideoEditingCardList();
        }

        try {
            String json = rawContent.trim();

            Matcher matcher = JSON_PATTERN.matcher(json);
            if (matcher.find()) {
                json = matcher.group();
                log.info("[VideoEditingCardsService] extracted JSON from response");
            } else {
                log.warn("[VideoEditingCardsService] no JSON found in response — returning empty list");
                log.warn("[VideoEditingCardsService] raw response was: '{}'", rawContent);
                return new VideoEditingCardList();
            }

            VideoEditingCardList result = objectMapper.readValue(json, VideoEditingCardList.class);
            log.info("[VideoEditingCardsService] parsed {} card(s)",
                    result.getCards() != null ? result.getCards().size() : 0);

            return result;
        } catch (Exception e) {
            log.error("[VideoEditingCardsService] failed to parse LLM response: {}", e.getMessage());
            log.error("[VideoEditingCardsService] raw response was: '{}'", rawContent);

            return new VideoEditingCardList();
        }
    }
}
