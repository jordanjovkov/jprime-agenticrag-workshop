package io.jprime.agenticrag.retriever.domain.llm.client.sync;

import io.jprime.agenticrag.retriever.domain.observability.LoggingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AgenticRAGClient implements ChatSyncClient {

    private static final Logger log = LoggerFactory.getLogger(AgenticRAGClient.class);

    private final ChatClient agenticChatClient;

    public AgenticRAGClient(@Qualifier("agenticChatClient") ChatClient agenticChatClient) {
        this.agenticChatClient = agenticChatClient;
    }

    @Override
    public String call(String prompt) {
        log.info("[LLM:agentic-rag] Sending prompt ({} chars): '{}'",
                prompt.length(), LoggingUtils.truncate(prompt));

        String content = agenticChatClient.prompt(prompt)
                .call()
                .content();

        if (content == null) {
            content = "";
        }

        log.info("[LLM:agentic-rag] Received final response ({} chars): '{}'",
                content.length(), LoggingUtils.truncate(content));

        return content;
    }
}
