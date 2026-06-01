package io.jprime.agenticrag.retriever.domain.llm.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Loads, caches, and provides all prompt templates used across the application.
 * <p>
 * Each prompt is loaded from an external file resource configured via
 * {@code application.properties}. If a resource cannot be loaded at startup,
 * a hardcoded backup variant is used as a fallback — this ensures the application
 * remains functional even when prompt files are missing or misconfigured.
 * A {@code WARN} log entry is emitted when a backup variant is active.
 * <p>
 * All prompts are pre-loaded and cached at startup via {@link PostConstruct}
 * to avoid repeated file I/O on every request.
 */
@Service
public class PromptService {

    private static final Logger log = LoggerFactory.getLogger(PromptService.class);

    @Value("${app.naiverag.prompttemplate}")
    private Resource naiveRAGPromptTemplateResource;

    @Value("${app.agenticrag.systemprompttemplate}")
    private Resource agenticRAGSystemPromptTemplateResource;

    @Value("${app.naiverag.videoeditingcards.prompt}")
    private Resource videoEditingCardsPromptResource;

    @Value("${app.naiverag.videoeditingcards.multidocument.prompt}")
    private Resource videoEditingCardsMultiDocumentPromptResource;

    private static final String NAIVE_RAG_PROMPT_TEMPLATE_BACKUP_VARIANT = """
                <query>
                
                Context information is below.
                
                ---------------------
                <question_answer_context>
                ---------------------
                
                Given the context information and no prior knowledge, answer the query.
                
                Follow these rules:
                
                1. If the answer is not in the context, just say that you don't know.
                2. Avoid statements like "Based on the context..." or "The provided information...".
                """;

    private static final String AGENTIC_RAG_SYSTEM_PROMPT_BACKUP_VARIANT = """
            You are an expert assistant on professional video processing hardware from the 1990s and early 2000s.
            
             You have access to two sources of information:
             1. A product catalog tool with prices and availability
             2. A knowledge base with technical specifications
    
             INSTRUCTIONS:
             1. Use the catalog tool when the user asks about prices, budget or availability
             2. Use the knowledge base for technical specifications and details
             3. Combine both sources when needed to give a complete answer
             4. Answer ONLY in the SAME LANGUAGE as the asked question language
             5. Answer clearly, accurately, and in a structured manner
             6. If there is contradictory information in the context, point it out
    
             IMPORTANT:
             - Use the catalog tool and knowledge base together when the question requires both
             - Be honest about the limitations of the information
             - If the context is partial or unclear, mention this
        """;

    private static final String VIDEO_EDITING_CARDS_PROMPT_BACKUP_VARIANT = """
            Give me a list of video editing cards by different manufacturers that can be used for digital video editing.
            The returned data must be their name, manufacturer and description (up to 10 words). If there is no data about a field - return NULL for this field.
            Return the result as a JSON object with a field "cards" that contains an array of objects.
            
                Example format:
                {
                    "cards": [
                        {
                            "name": "Card 1",
                            "manufacturer": "Manufacturer 1",
                            "description": "Description 1"
                        },
                        {
                            "name": "Card 2",
                            "manufacturer": "Manufacturer 2",
                            "description": "Description 2"
                        }
                    ]
                }
            """;

    private static final String VIDEO_EDITING_CARDS_MULTI_DOCUMENT_PROMPT_BACKUP_VARIANT = """
            Give me a list of video editing cards by different manufacturers that can be used for digital video editing.
            The returned data must be their name, manufacturer and description (up to 10 words). If there is no data about a field - return NULL for this field.
            Return the result as a JSON object with a field "cards" that contains an array of objects.
            
            Use ONLY the information from the context below:
            ---------------------
            {context}
            ---------------------
            
                Example format:
                {
                    "cards": [
                        {
                            "name": "Card 1",
                            "manufacturer": "Manufacturer 1",
                            "description": "Description 1"
                        },
                        {
                            "name": "Card 2",
                            "manufacturer": "Manufacturer 2",
                            "description": "Description 2"
                        }
                    ]
                }
            """;

    private PromptTemplate cachedNaiveRAGPromptTemplate;
    private String cachedAgenticRAGSystemPrompt;
    private String cachedVideoEditingCardsPrompt;
    private String cachedVideoEditingCardsMultiDocumentPrompt;

    /**
     * Loads all prompt templates from their configured file resources and caches them.
     * Falls back to hardcoded backup variants for any resource that fails to load.
     */
    @PostConstruct
    void initPromptTemplates() {
        log.info("Initializing and caching prompt templates...");

        cachedNaiveRAGPromptTemplate =
                buildPromptTemplate(naiveRAGPromptTemplateResource, NAIVE_RAG_PROMPT_TEMPLATE_BACKUP_VARIANT);

        cachedAgenticRAGSystemPrompt =
                loadResourceText(agenticRAGSystemPromptTemplateResource, AGENTIC_RAG_SYSTEM_PROMPT_BACKUP_VARIANT);

        cachedVideoEditingCardsPrompt =
                loadResourceText(videoEditingCardsPromptResource, VIDEO_EDITING_CARDS_PROMPT_BACKUP_VARIANT);

        cachedVideoEditingCardsMultiDocumentPrompt =
                loadResourceText(videoEditingCardsMultiDocumentPromptResource,
                        VIDEO_EDITING_CARDS_MULTI_DOCUMENT_PROMPT_BACKUP_VARIANT);

        log.info("Prompt templates initialized successfully.");
    }

    /**
     * Loads the text content of a {@link Resource}. Returns the backup variant
     * if the resource cannot be read, and logs a warning to make the fallback visible.
     */
    private String loadResourceText(Resource resource, String resourceTextBackupVariant) {
        try (InputStream is = resource.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("BACKUP PROMPT ACTIVE — Failed to load prompt template from: {}. Using hardcoded fallback!",
                    resource.getDescription(), e);
            return resourceTextBackupVariant;
        }
    }

    /**
     * Builds a {@link PromptTemplate} from a {@link Resource}, using a custom
     * {@link StTemplateRenderer} with {@code <} and {@code >} as delimiters
     * instead of the Spring AI default {@code {} and {@code }}.
     * This avoids conflicts with JSON examples embedded in the prompt text.
     */
    private PromptTemplate buildPromptTemplate(Resource resource, String promptTextBackupVariant) {
        String promptTemplateText = loadResourceText(resource, promptTextBackupVariant);

        return PromptTemplate
                .builder()
                .renderer(StTemplateRenderer
                        .builder()
                        .startDelimiterToken('<')
                        .endDelimiterToken('>')
                        .build())
                .template(promptTemplateText)
                .build();
    }

    public PromptTemplate getNaiveRAGPromptTemplate() {
        return cachedNaiveRAGPromptTemplate;
    }

    public String getAgenticRAGSystemPrompt() {
        return cachedAgenticRAGSystemPrompt;
    }

    public String getVideoEditingCardsPrompt() {
        return cachedVideoEditingCardsPrompt;
    }

    public String getVideoEditingCardsMultiDocumentPrompt() {
        return cachedVideoEditingCardsMultiDocumentPrompt;
    }
}
