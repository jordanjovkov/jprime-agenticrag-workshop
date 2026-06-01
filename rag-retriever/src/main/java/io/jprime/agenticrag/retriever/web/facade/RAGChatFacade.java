package io.jprime.agenticrag.retriever.web.facade;

import io.jprime.agenticrag.retriever.domain.llm.client.LLMChatMode;
import io.jprime.agenticrag.retriever.domain.llm.client.sync.ChatSyncClient;
import io.jprime.agenticrag.retriever.domain.llm.factory.ChatClientFactory;
import org.springframework.stereotype.Component;

@Component
public class RAGChatFacade {

    private final ChatClientFactory chatClientFactory;

    public RAGChatFacade(ChatClientFactory chatClientFactory) {
        this.chatClientFactory = chatClientFactory;
    }

    public String askNaiveRAG(String prompt) {
        ChatSyncClient chatSyncClient = chatClientFactory.createSyncClient(LLMChatMode.NAIVE_RAG);
        return chatSyncClient.call(prompt);
    }
}
