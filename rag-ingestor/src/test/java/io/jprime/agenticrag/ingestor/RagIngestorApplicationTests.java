package io.jprime.agenticrag.ingestor;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// TODO: Add unit tests with Mockito for service classes:
//       - KnowledgeBaseService
//       - DocumentReaderService
//       - DocumentTransformerService
//       - DocumentWriterService

// TODO: Add integration tests with Testcontainers (pgvector) for CI:
//       - VectorRepository
//       - KnowledgeBaseService (full ETL pipeline)
@Disabled("Requires pgvector Docker container")
@SpringBootTest
class RagIngestorApplicationTests {

    @Test
    void contextLoads() {

    }
}