package io.jprime.agenticrag.retriever.web.facade;

import io.jprime.agenticrag.retriever.domain.llm.client.LLMChatMode;
import io.jprime.agenticrag.retriever.domain.llm.client.sync.ChatSyncClient;
import io.jprime.agenticrag.retriever.domain.llm.factory.ChatClientFactory;
import org.springframework.stereotype.Component;

@Component
public class DirectLLMFacade {

    private final ChatClientFactory chatClientFactory;

    public DirectLLMFacade(ChatClientFactory chatClientFactory) {
        this.chatClientFactory = chatClientFactory;
    }

    public String prompt(String prompt) {
        ChatSyncClient chatSyncClient = chatClientFactory.createSyncClient(LLMChatMode.SIMPLE_LLM);
        return chatSyncClient.call(prompt);
    }
}
