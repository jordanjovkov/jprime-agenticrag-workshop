package io.jprime.agenticrag.retriever.web.controller;

import io.jprime.agenticrag.retriever.domain.observability.LoggingUtils;
import io.jprime.agenticrag.retriever.web.facade.RAGChatFacade;
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
@RequestMapping("/agentic-rag")
@Validated
public class AgenticRAGController {

    private static final Logger log = LoggerFactory.getLogger(AgenticRAGController.class);

    private final RAGChatFacade ragChatFacade;

    public AgenticRAGController(RAGChatFacade ragChatFacade) {
        this.ragChatFacade = ragChatFacade;
    }

    @GetMapping(value = "/ask")
    public String askAgenticRAG(
            @RequestParam(value = "prompt")
            @NotBlank(message = "Prompt cannot be blank.")
            @Size(max = 4096, message = "Prompt must not exceed 4096 characters.")
            String prompt) {
        log.info("[AgenticRAGController] GET /ask — prompt ({} chars): '{}'",
                prompt.length(), LoggingUtils.truncate(prompt));

        return ragChatFacade.askAgenticRAG(prompt);
    }
}
