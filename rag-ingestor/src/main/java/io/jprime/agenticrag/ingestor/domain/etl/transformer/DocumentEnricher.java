package io.jprime.agenticrag.ingestor.domain.etl.transformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ETL Transform sub-step — placeholder for document enrichment before vector storage.
 * <p>
 * Currently a pass-through — documents are returned unchanged.
 * This step exists as an explicit extension point in the pipeline for future enrichment,
 * such as:
 * <ul>
 *   <li>Adding domain-specific metadata (product category, document version)</li>
 *   <li>Language detection and tagging</li>
 *   <li>Content normalization or filtering</li>
 * </ul>
 */
@Component
public class DocumentEnricher {

    private static final Logger log = LoggerFactory.getLogger(DocumentEnricher.class);

    public List<Document> enrichDocuments(List<Document> documents) {

        // TODO: Enrich documents with additional information on the next stage of the pipeline

        log.info("[ETL:Enrich] Document enrichment step skipped (not yet implemented)");

        return documents;
    }
}
