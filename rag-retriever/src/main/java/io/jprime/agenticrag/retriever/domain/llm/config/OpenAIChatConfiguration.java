package io.jprime.agenticrag.retriever.domain.llm.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configures the OpenAI-compatible chat model bean used for LLM interactions.
 * <p>
 * Active only under the {@code groq-chat-ollama-embed} profile, which combines
 * Groq as the chat LLM provider with Ollama as the local embedding provider.
 * <p>
 * Although the bean uses {@link OpenAiChatModel}, it points to the Groq API endpoint
 * via {@code base-url} — Groq exposes an OpenAI-compatible REST API, so no
 * Groq-specific client is needed.
 */
@Configuration
@Profile("groq-chat-ollama-embed")
public class OpenAIChatConfiguration {

    /**
     * Creates a {@link ChatModel} backed by Groq via the OpenAI-compatible API.
     * <p>
     * Model, base URL, API key, and temperature are externalized via
     * {@code application.properties} and injected at startup.
     * The workshop default uses {@code openai/gpt-oss-120b} via the Groq free tier.
     */
    @Bean
    public ChatModel openAiChatModel(
            @Value("${spring.ai.openai.base-url}") String baseUrl,
            @Value("${spring.ai.openai.api-key}") String apiKey,
            @Value("${spring.ai.openai.chat.options.model}") String model,
            @Value("${spring.ai.openai.chat.options.temperature}") Double temperature) {

        var options = OpenAiChatOptions.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .model(model)
                .temperature(temperature)
                .build();

        return OpenAiChatModel.builder()
                .options(options)
                .build();
    }

}
