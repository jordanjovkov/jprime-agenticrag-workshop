package io.jprime.agenticrag.retriever.domain.observability;

import io.micrometer.context.ThreadLocalAccessor;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;

/**
 * Bridges OTel's Context into Micrometer's context-propagation so that
 * Hooks.enableAutomaticContextPropagation() propagates the active OTel span
 * (and therefore the correct parent trace) to Reactor worker threads.
 *
 * Without this, ContextSnapshot.captureAll() captures only the Micrometer
 * Observation but not OTel's Context.current(). When QuestionAnswerAdvisor
 * runs on a boundedElastic thread, Context.current() is empty, so every span
 * it creates becomes a root trace instead of a child of the HTTP request span.
 */
public class OtelContextAccessor implements ThreadLocalAccessor<Context> {

    public static final Object KEY = Context.class;

    // Holds the Scope opened by makeCurrent() so we can close it on reset.
    // Each thread has its own slot — no shared state.
    private static final ThreadLocal<Scope> SCOPE_HOLDER = new ThreadLocal<>();

    @Override
    public Object key() {
        return KEY;
    }

    @Override
    public Context getValue() {
        return Context.current();
    }

    @Override
    public void setValue(Context value) {
        Scope newScope = value.makeCurrent();
        Scope existing = SCOPE_HOLDER.get();
        if (existing != null) {
            existing.close();
        }
        SCOPE_HOLDER.set(newScope);
    }

    @Override
    public void setValue() {
        Scope scope = SCOPE_HOLDER.get();
        if (scope != null) {
            scope.close();
            SCOPE_HOLDER.remove();
        }
    }
}