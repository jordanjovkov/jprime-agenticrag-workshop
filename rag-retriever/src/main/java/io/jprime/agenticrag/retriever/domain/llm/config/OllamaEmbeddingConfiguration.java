package io.jprime.agenticrag.retriever.domain.llm.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configures the Ollama embedding model bean used for vector similarity search.
 * <p>
 * Active only under the {@code groq-chat-ollama-embed} profile, which combines
 * Groq as the chat LLM provider with Ollama as the local embedding provider.
 * <p>
 * A programmatic {@link OllamaApi} bean is constructed explicitly (rather than
 * relying on Spring AI auto-configuration) to allow setting a custom
 * {@code readTimeout} — required because embedding large batches of text chunks
 * can exceed the default timeout.
 */
@Configuration
@Profile("groq-chat-ollama-embed")
public class OllamaEmbeddingConfiguration {

    /**
     * Creates an {@link EmbeddingModel} backed by Ollama.
     * <p>
     * Model, base URL, and batch size are externalized via {@code application.properties}
     * and injected at startup. The workshop default uses {@code nomic-embed-text}
     * with 768 dimensions and a chunk size of 512 tokens.
     */
    @Bean
    public EmbeddingModel ollamaEmbeddingModel(
            @Value("${spring.ai.ollama.base-url}") String baseUrl,
            @Value("${spring.ai.ollama.embedding.options.model}") String embeddingModel,
            @Value("${spring.ai.ollama.embedding.options.num-batch}") Integer batchSize) {

        var ollamaApi = OllamaApi.builder()
                .baseUrl(baseUrl)
                .build();

        var options = org.springframework.ai.ollama.api.OllamaEmbeddingOptions.builder()
                .model(embeddingModel)
                .numBatch(batchSize)
                .build();

        return OllamaEmbeddingModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(options)
                .build();
    }

}
