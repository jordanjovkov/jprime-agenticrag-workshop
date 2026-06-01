package io.jprime.agenticrag.retriever.domain.llm.client.config;

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
 *       Two conditional variants exist depending on {@code app.tools.mode}:
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