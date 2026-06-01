package io.jprime.agenticrag.retriever.domain.llm.factory;

import io.jprime.agenticrag.retriever.domain.llm.client.LLMChatMode;
import io.jprime.agenticrag.retriever.domain.llm.client.sync.ChatSyncClient;
import io.jprime.agenticrag.retriever.domain.llm.client.sync.NaiveRAGClient;
import io.jprime.agenticrag.retriever.domain.llm.client.sync.SimpleLLMClient;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Factory for resolving the correct {@link ChatSyncClient} implementation
 * based on the requested {@link LLMChatMode}.
 * <p>
 * Acts as a single dispatch point — controllers and facades request a client
 * by mode without being coupled to the concrete implementation classes.
 * <p>
 * Currently all modes operate synchronously (request-response).
 * If streaming support is introduced in the future, a separate
 * {@code ChatStreamClient} factory should be added alongside this one.
 */
@Component
public class ChatClientFactory {

    private final Map<LLMChatMode, ChatSyncClient> chatSyncClientTypes;

    public ChatClientFactory(SimpleLLMClient simpleLLM,
                             NaiveRAGClient naiveRAG) {

        this.chatSyncClientTypes = Map.of(
                LLMChatMode.SIMPLE_LLM, simpleLLM,
                LLMChatMode.NAIVE_RAG, naiveRAG
        );
    }

    /**
     * Returns the {@link ChatSyncClient} registered for the given {@link LLMChatMode}.
     *
     * @param type the requested interaction mode
     * @return the corresponding sync client
     * @throws UnsupportedOperationException if no client is registered for the given mode
     */
    public ChatSyncClient createSyncClient(LLMChatMode type) {
        ChatSyncClient chatSyncClient = chatSyncClientTypes.get(type);

        if (chatSyncClient == null) {
            throw new UnsupportedOperationException("Unsupported RequestType: " + type);
        }
        return chatSyncClient;
    }
}
