package io.jprime.agenticrag.ingestor.domain.etl.transformer;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ETL Transform step — splits raw documents into smaller chunks suitable for vector embedding.
 * <p>
 * Chunking is a critical parameter in RAG quality. Chunk size directly affects:
 * <ul>
 *   <li><b>Retrieval precision</b> — smaller chunks are more targeted but may lose context</li>
 *   <li><b>Embedding quality</b> — chunks exceeding the embedding model's token limit are truncated</li>
 *   <li><b>Search relevance</b> — chunk boundaries determine what context the LLM receives</li>
 * </ul>
 * The workshop default uses {@code chunkSize=512} tokens, matching the {@code nomic-embed-text}
 * embedding model's optimal input window. Changing this value and re-ingesting the documents
 * is one of the key experiments demonstrated in Step 3 of the workshop.
 * <p>
 * All parameters are externalized via {@code application.properties} under the
 * {@code app.etl.textsplitter.*} namespace and pre-built at startup via {@link PostConstruct}.
 */
@Component
public class ETLTokenTextSplitter {

    private static final Logger log = LoggerFactory.getLogger(ETLTokenTextSplitter.class);

    @Value("${app.etl.textsplitter.chunksize:512}")
    private int chunkSize;

    @Value("${app.etl.textsplitter.minchunksizechars:10}")
    private int withMinChunkSizeChars;

    @Value("${app.etl.textsplitter.minchunklengthtoembed:10}")
    private int minChunkLengthToEmbed;

    @Value("${app.etl.textsplitter.maxnumchunks:5000}")
    private int maxNumChunks;

    @Value("${app.etl.textsplitter.keepseparator:true}")
    private boolean keepSeparator;

    private TokenTextSplitter splitter;

    /**
     * Builds and caches the {@link TokenTextSplitter} at startup with the configured parameters.
     */
    @PostConstruct
    void initSplitter() {
        splitter = TokenTextSplitter.builder()
                .withChunkSize(chunkSize)
                .withMinChunkSizeChars(withMinChunkSizeChars)
                .withMinChunkLengthToEmbed(minChunkLengthToEmbed)
                .withMaxNumChunks(maxNumChunks)
                .withKeepSeparator(keepSeparator)
                .build();
        log.info("TokenTextSplitter initialized with chunkSize={}, maxNumChunks={}", chunkSize, maxNumChunks);
    }

    /**
     * Splits the given documents into token-based chunks.
     * Each input document may produce one or more output chunks depending on its size
     * relative to {@code chunkSize}. Metadata from the original document is preserved
     * in each resulting chunk.
     *
     * @param documents the raw documents to split
     * @return list of chunked {@link Document} instances ready for embedding and storage
     */
    public List<Document> splitDocuments(List<Document> documents) {
        List<Document> splitDocumentParts = splitter.apply(documents);
        log.info("Loaded document data split into {} chunks", splitDocumentParts.size());
        return splitDocumentParts;
    }
}
