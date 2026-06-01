package io.jprime.agenticrag.retriever.domain.model.constant;

import java.util.List;

/**
 * Constants for the names of video editing cards in the Knowledge Base and the store.
 * Used for targeted per-document Knowledge Base searches.
 * Not instantiable — use constant directly.
 */
public final class VideoEditingCardNames {

    public static final String MOVIE_MACHINE_PRO = "Movie Machine Pro";
    public static final String DPS_VELOCITY = "DPS Velocity";
    public static final String MEDIA_100 = "Media 100";
    public static final String MIRO_MOTION_DC30 = "MiroMotion DC30";

    private VideoEditingCardNames() {
        throw new UnsupportedOperationException("VideoEditingCardNames is a utility class and cannot be instantiated.");
    }

    /**
     * Returns all video editing card names in the Knowledge Base.
     * Used for targeted per-document searches in multi-document RAG queries.
     */
    public static List<String> getAll() {
        return List.of(MOVIE_MACHINE_PRO, DPS_VELOCITY, MEDIA_100, MIRO_MOTION_DC30);
    }
}
