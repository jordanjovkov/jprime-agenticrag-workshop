package io.jprime.agenticrag.retriever.domain.llm.config;

import io.jprime.agenticrag.retriever.domain.llm.mcp.McpConnectionManager;
import io.jprime.agenticrag.retriever.domain.llm.service.PromptService;
import io.jprime.agenticrag.retriever.domain.llm.tool.CustomerQueryTools;
import io.jprime.agenticrag.retriever.domain.llm.tool.KnowledgeBaseQueryTools;
import io.jprime.agenticrag.retriever.domain.llm.tool.OrderQueryTools;
import io.jprime.agenticrag.retriever.domain.llm.tool.VideoEditingCardQueryTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the {@link ChatClient} beans used across the different LLM interaction modes.
 * <p>
 * Three named clients are registered:
 * <ul>
 *   <li><b>simpleChatClient</b> — plain LLM call with no additional context or tools</li>
 *   <li><b>naiveChatClient</b> — used for Naive RAG; augmented at call time via {@link org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor}</li>
 *   <li><b>agenticChatClient</b> — used for Agentic RAG; configured with a system prompt and tools.
 *       Two conditional variants exist depending on {@code app.tools.mode}:
 *       <ul>
 *         <li>{@code local} (default) — tools are local Spring beans</li>
 *         <li>{@code mcp} — tools are provided by an external MCP server</li>
 *       </ul>
 *   </li>
 * </ul>
 */
@Configuration
public class ChatClientConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ChatClientConfiguration.class);

    private final PromptService promptService;

    public ChatClientConfiguration(PromptService promptService) {
        this.promptService = promptService;
    }

    /**
     * Plain {@link ChatClient} with no system prompt and no tools.
     * Used for direct LLM calls to demonstrate raw model behavior (Step 2).
     */
    @Bean
    @Qualifier("simpleChatClient")
    public ChatClient simpleChatClient(ChatClient.Builder builder) {
        log.info("[ChatClientConfiguration] Creating simpleChatClient bean");
        return builder.build();
    }

    /**
     * {@link ChatClient} for Naive RAG (Step 4).
     * No tools or system prompt configured here — the {@link org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor}
     * is attached dynamically at call time in {@link io.jprime.agenticrag.retriever.domain.llm.client.sync.NaiveRAGClient}.
     */
    @Bean
    @Qualifier("naiveChatClient")
    public ChatClient naiveChatClient(ChatClient.Builder builder) {
        log.info("[ChatClientConfiguration] Creating naiveChatClient bean");
        return builder.build();
    }

    /**
     * {@link ChatClient} for Agentic RAG using local tool beans (Steps 5 and 6).
     * Active when {@code app.tools.mode=local} (default).
     * <p>
     * All four tool classes are registered directly as Spring beans —
     * the agent autonomously decides which tools to invoke and how many times.
     */
    @Bean
    @Qualifier("agenticChatClient")
    @ConditionalOnProperty(name = "app.tools.mode", havingValue = "local", matchIfMissing = true)
    public ChatClient agenticChatClientLocal(ChatClient.Builder builder,
                                             VideoEditingCardQueryTools videoEditingCardQueryTools,
                                             CustomerQueryTools customerQueryTools,
                                             OrderQueryTools orderQueryTools,
                                             KnowledgeBaseQueryTools knowledgeBaseQueryTools) {

        log.info("[ChatClientConfiguration] Creating agenticChatClient bean — mode: local tool");

        String systemPrompt = promptService.getAgenticRAGSystemPrompt();

        return builder
                .defaultSystem(systemPrompt)
                .defaultTools(videoEditingCardQueryTools, customerQueryTools, orderQueryTools, knowledgeBaseQueryTools)
                .build();
    }

    /**
     * {@link ChatClient} for Agentic RAG using an external MCP server (Step 7).
     * Active when {@code app.tools.mode=mcp}.
     * <p>
     * The video production store tools are discovered and invoked via the MCP protocol —
     * the agent treats the MCP server as a black box with no knowledge of its internals.
     * {@link KnowledgeBaseQueryTools} remains local in both configurations.
     */
    @Bean
    @Qualifier("agenticChatClient")
    @ConditionalOnProperty(name = "app.tools.mode", havingValue = "mcp")
    public ChatClient agenticChatClientMcp(ChatClient.Builder builder,
                                           KnowledgeBaseQueryTools knowledgeBaseQueryTools,
                                           McpConnectionManager mcpConnectionManager) {

        log.info("[ChatClientConfiguration] Creating agenticChatClient bean — mode: MCP tools");

        String systemPrompt = promptService.getAgenticRAGSystemPrompt();
        ToolCallbackProvider videoProductionStoreMCPTools = mcpConnectionManager.getToolCallbackProvider();

        return builder
                .defaultSystem(systemPrompt)
                .defaultTools(knowledgeBaseQueryTools)
                .defaultToolCallbacks(videoProductionStoreMCPTools)
                .build();
    }
}
