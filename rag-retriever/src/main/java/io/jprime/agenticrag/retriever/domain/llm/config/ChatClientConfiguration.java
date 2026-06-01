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
}