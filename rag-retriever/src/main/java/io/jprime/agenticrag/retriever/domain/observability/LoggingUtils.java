package io.jprime.agenticrag.retriever.domain.observability;

/**
 * Utility class providing shared logging helper methods.
 * Not instantiable — use static methods directly.
 */
public final class LoggingUtils {

    public static final int LOG_TRUNCATE_LENGTH = 200;

    private LoggingUtils() {
        throw new UnsupportedOperationException("LoggingUtils is a utility class and cannot be instantiated.");
    }

    /**
     * Truncates the given text to {@link #LOG_TRUNCATE_LENGTH} characters for log output.
     * Returns an empty string if the input is null.
     */
    public static String truncate(String text) {
        if (text == null) return "";
        return text.length() <= LOG_TRUNCATE_LENGTH ? text : text.substring(0, LOG_TRUNCATE_LENGTH) + "...";
    }
}
