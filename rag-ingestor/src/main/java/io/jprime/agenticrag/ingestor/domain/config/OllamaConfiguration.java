package io.jprime.agenticrag.ingestor.domain.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaEmbeddingOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configures the Ollama embedding model bean used for vector similarity search.
 * <p>
 * Active only under the {@code manual-bean-configuration} profile, which bypasses
 * Spring AI auto-configuration and constructs the embedding model explicitly.
 * This is required to allow setting a custom {@code readTimeout} — necessary because
 * embedding large batches of text chunks can exceed the default timeout.
 * <p>
 * The workshop default uses {@code nomic-embed-text} with 768 dimensions
 * and a chunk size of 512 tokens.
 */
@Configuration
@Profile("manual-bean-configuration")
public class OllamaConfiguration {

    /**
     * Creates an {@link EmbeddingModel} backed by Ollama.
     * Model, base URL, and batch size are externalized via {@code application.properties}.
     */
    @Bean
    public EmbeddingModel ollamaEmbeddingModel(
            @Value("${spring.ai.ollama.base-url}") String baseUrl,
            @Value("${spring.ai.ollama.embedding.options.model}") String embeddingModel,
            @Value("${spring.ai.ollama.embedding.options.num-batch}") int batchSize) {

        var ollamaApi = OllamaApi.builder()
                .baseUrl(baseUrl)
                .build();

        var options = OllamaEmbeddingOptions.builder()
                .model(embeddingModel)
                .numBatch(batchSize)
                .build();

        return OllamaEmbeddingModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(options)
                .build();
    }
}
