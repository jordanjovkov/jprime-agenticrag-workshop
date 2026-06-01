package io.jprime.agenticrag.retriever.domain.llm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Bean
    @Qualifier("simpleChatClient")
    public ChatClient simpleChatClient(ChatClient.Builder builder) {
        log.info("[ChatClientConfiguration] Creating simpleChatClient bean");
        return builder.build();
    }
}