package io.jprime.agenticrag.retriever.web.controller;

import io.jprime.agenticrag.retriever.domain.model.llmresponse.VideoEditingCardList;
import io.jprime.agenticrag.retriever.domain.observability.LoggingUtils;
import io.jprime.agenticrag.retriever.web.facade.RAGChatFacade;
import io.jprime.agenticrag.retriever.web.facade.VideoEditingCardsFacade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/naive-rag")
@Validated
public class NaiveRAGController {

    private static final Logger log = LoggerFactory.getLogger(NaiveRAGController.class);

    private final RAGChatFacade ragChatFacade;
    private final VideoEditingCardsFacade videoEditingCardsFacade;

    public NaiveRAGController(RAGChatFacade ragChatFacade,
                              VideoEditingCardsFacade videoEditingCardsFacade) {
        this.ragChatFacade = ragChatFacade;
        this.videoEditingCardsFacade = videoEditingCardsFacade;
    }

    @GetMapping(value = "/ask")
    public String askRAG(
            @RequestParam(value = "prompt")
            @NotBlank(message = "Prompt cannot be blank.")
            @Size(max = 4096, message = "Prompt must not exceed 4096 characters.")
            String prompt) {
        log.info("[NaiveRAGController] GET /ask — prompt ({} chars): '{}'",
                prompt.length(), LoggingUtils.truncate(prompt));

        return ragChatFacade.askNaiveRAG(prompt);
    }

    /**
     * Naive RAG approach — demonstrates the single-document limitation.
     * The full prompt is used as the pgvector search query, which results in
     * retrieving chunks from only one document due to embedding similarity bias.
     */
    @GetMapping(value = "/video-editing-cards")
    public VideoEditingCardList getVideoEditingCards() {
        log.info("[NaiveRAGController] GET /video-editing-cards");

        return videoEditingCardsFacade.getVideoEditingCardList();
    }

    /**
     * Multi-document RAG approach — solves the single-document limitation.
     * Performs a targeted Knowledge Base search per video editing card,
     * combines all retrieved chunks into a single context, and passes it
     * directly to the LLM — ensuring all documents contribute to the result.
     */
    @GetMapping(value = "/video-editing-cards-multi-document")
    public VideoEditingCardList getVideoEditingCardsMultiDocument() {
        log.info("[NaiveRAGController] GET /video-editing-cards-multi-document");

        return videoEditingCardsFacade.getVideoEditingCardListMultiDocument();
    }
}
