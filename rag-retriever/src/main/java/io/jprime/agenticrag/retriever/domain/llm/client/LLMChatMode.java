package io.jprime.agenticrag.retriever.domain.llm.client;

/**
 * Defines the available LLM interaction modes.
 * <p>
 * Currently all modes operate synchronously (request-response).
 * <p>
 * If streaming support is introduced in the future, new mode types
 * should be added here and a {@code stream} sub-package created
 * alongside the existing {@code sync} sub-package.
 */
public enum LLMChatMode {
    SIMPLE_LLM,
    NAIVE_RAG,
    AGENTIC_RAG
}