package io.jprime.agenticrag.retriever.domain.llm.client.sync;

import io.jprime.agenticrag.retriever.domain.llm.factory.RAGAdvisorFactory;
import io.jprime.agenticrag.retriever.domain.observability.LoggingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class NaiveRAGClient implements ChatSyncClient {

    private static final Logger log = LoggerFactory.getLogger(NaiveRAGClient.class);

    private final ChatClient naiveChatClient;
    private final RAGAdvisorFactory advisorFactory;

    public NaiveRAGClient(@Qualifier("naiveChatClient") ChatClient naiveChatClient,
                          RAGAdvisorFactory advisorFactory) {
        this.naiveChatClient = naiveChatClient;
        this.advisorFactory = advisorFactory;
    }

    @Override
    public String call(String prompt) {
        log.info("[LLM:naive-rag] Sending prompt ({} chars): '{}'",
                prompt.length(), LoggingUtils.truncate(prompt));

        QuestionAnswerAdvisor naiveAdvisor = advisorFactory.createNaiveAdvisor();

        String content = naiveChatClient.prompt(prompt)
                .advisors(naiveAdvisor)
                .call()
                .content();

        if (content == null) {
            content = "";
        }

        log.info("[LLM:naive-rag] Received response ({} chars): '{}'",
                content.length(), LoggingUtils.truncate(content));

        return content;
    }
}
