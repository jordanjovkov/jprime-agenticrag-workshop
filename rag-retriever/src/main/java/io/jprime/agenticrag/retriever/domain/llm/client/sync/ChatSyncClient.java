package io.jprime.agenticrag.retriever.domain.llm.client.sync;

/**
 * Contract for synchronous LLM interactions (request-response).
 * <p>
 * Each implementation in the {@code sync} package corresponds to a specific
 * {@link io.jprime.agenticrag.retriever.domain.llm.client.LLMChatMode}
 * and encapsulates the prompt execution logic for that mode:
 * <ul>
 *   <li>{@link SimpleLLMClient} — direct LLM call with no context or tools</li>
 *   <li>{@link NaiveRAGClient} — LLM call augmented with vector store context via advisor</li>
 *   <li>{@link AgenticRAGClient} — LLM call with tools, enabling autonomous multi-step reasoning</li>
 * </ul>
 * <p>
 * If streaming support is introduced in the future, a separate {@code ChatStreamClient}
 * interface should be added alongside this one in a new {@code stream} sub-package.
 */
public interface ChatSyncClient {

    /**
     * Sends the given prompt to the LLM and returns the response as a plain string.
     *
     * @param prompt the user prompt to send
     * @return the LLM response content, never {@code null} (empty string if the model returns nothing)
     */
    String call(String prompt);
}
