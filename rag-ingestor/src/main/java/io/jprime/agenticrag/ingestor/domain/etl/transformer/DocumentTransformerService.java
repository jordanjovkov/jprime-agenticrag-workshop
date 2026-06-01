package io.jprime.agenticrag.ingestor.domain.etl.transformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ETL Transform step — orchestrates all document transformation operations
 * between the Read and Write phases of the ingestion pipeline.
 * <p>
 * Currently applies two sub-steps in sequence:
 * <ol>
 *   <li><b>Split</b> — chunks documents into token-sized fragments via {@link ETLTokenTextSplitter}</li>
 *   <li><b>Enrich</b> — placeholder for future metadata enrichment via {@link DocumentEnricher}</li>
 * </ol>
 * New transformation steps (e.g. deduplication, language detection, content filtering)
 * can be added here without touching the rest of the pipeline.
 */
@Component
public class DocumentTransformerService {

    private static final Logger log = LoggerFactory.getLogger(DocumentTransformerService.class);

    private final ETLTokenTextSplitter textSplitter;
    private final DocumentEnricher documentEnricher;

    public DocumentTransformerService(ETLTokenTextSplitter textSplitter,
                                      DocumentEnricher documentEnricher) {
        this.textSplitter = textSplitter;
        this.documentEnricher = documentEnricher;
    }

    public List<Document> transformDocuments(List<Document> documents) {
        log.info("[ETL:Transform] Starting transformation of {} document(s)", documents.size());

        List<Document> splitDocuments = textSplitter.splitDocuments(documents);
        log.info("[ETL:Transform] After split: {} chunk(s)", splitDocuments.size());

        List<Document> enrichedDocuments = documentEnricher.enrichDocuments(splitDocuments);
        log.info("[ETL:Transform] After enrich: {} chunk(s) — transformation complete", enrichedDocuments.size());

        return enrichedDocuments;
    }
}
