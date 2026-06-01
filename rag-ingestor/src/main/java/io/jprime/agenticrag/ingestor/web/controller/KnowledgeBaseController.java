package io.jprime.agenticrag.ingestor.web.controller;

import io.jprime.agenticrag.ingestor.domain.service.KnowledgeBaseService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/knowledge-base")
@Validated
public class KnowledgeBaseController {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseController.class);

    private final KnowledgeBaseService knowledgeBaseService;

    public KnowledgeBaseController(KnowledgeBaseService knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }

    @PostMapping("/reload")
    public String reloadKnowledgeBase() {
        log.info("[KnowledgeBaseController] POST /reload");

        knowledgeBaseService.reloadKnowledgeBase();
        return "RAG Knowledge base reloaded!";
    }

    @GetMapping("/search")
    public String searchInKnowledgeBase(
            @RequestParam(value = "query")
            @NotBlank(message = "Search query cannot be blank.")
            @Size(max = 4096, message = "Search query must not exceed 4096 characters.")
            String searchQuery) {
        log.info("[KnowledgeBaseController] GET /search — query: '{}'", searchQuery);

        String foundSimilarText = knowledgeBaseService.searchAndFormatResults(searchQuery);

        StringBuilder searchResult = new StringBuilder("Searching in knowledge base:");
        searchResult.append("\nQuery: ");
        searchResult.append(searchQuery);
        searchResult.append("\nFound similar text: ");
        searchResult.append(foundSimilarText);

        return searchResult.toString();
    }
}
