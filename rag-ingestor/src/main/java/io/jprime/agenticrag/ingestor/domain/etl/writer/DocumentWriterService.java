package io.jprime.agenticrag.ingestor.domain.etl.writer;

import io.jprime.agenticrag.ingestor.persistence.VectorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ETL Write step — persists transformed document chunks to the vector store.
 * <p>
 * Acts as a facade over the persistence layer, decoupling the ETL pipeline
 * from the concrete storage implementation. Additional write targets
 * (e.g. relational database, search index) can be added here without
 * changing the upstream pipeline.
 */
@Service
public class DocumentWriterService {

   private static final Logger log = LoggerFactory.getLogger(DocumentWriterService.class);

   private final VectorRepository vectorRepository;

   public DocumentWriterService(VectorRepository vectorRepository) {
       this.vectorRepository = vectorRepository;
   }

    /**
     * Writes the given document chunks to all configured store targets.
     * Currently writes only to the vector store via {@link VectorRepository}.
     *
     * @param documents the chunked and enriched documents to persist
     */
   public void writeDocuments(List<Document> documents) {
       log.info("[ETL:Write] Starting write of {} document chunk(s) to vector store", documents.size());
       saveToVectorStore(documents);
       log.info("[ETL:Write] Write complete");
   }

   private void saveToVectorStore(List<Document> documents) {
       vectorRepository.save(documents);
    }

}
