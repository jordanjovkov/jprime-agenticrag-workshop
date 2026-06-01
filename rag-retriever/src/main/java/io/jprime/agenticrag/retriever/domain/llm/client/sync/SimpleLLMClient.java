package io.jprime.agenticrag.retriever.domain.llm.client.sync;

import io.jprime.agenticrag.retriever.domain.observability.LoggingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SimpleLLMClient implements ChatSyncClient {

    private static final Logger log = LoggerFactory.getLogger(SimpleLLMClient.class);

    private final ChatClient simpleChatClient;

    public SimpleLLMClient(@Qualifier("simpleChatClient") ChatClient simpleChatClient) {
        this.simpleChatClient = simpleChatClient;
    }

    @Override
    public String call(String prompt) {
        log.info("[LLM:simple] Sending prompt ({} chars): '{}'",
                prompt.length(), LoggingUtils.truncate(prompt));

        String content = simpleChatClient.prompt()
                .user(prompt)
                .call()
                .content();

        if (content == null) {
            content = "";
        }

        log.info("[LLM:simple] Received response ({} chars): '{}'",
                content.length(), LoggingUtils.truncate(content));

        return content;
    }
}
