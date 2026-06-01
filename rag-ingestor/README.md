# rag-ingestor

RAG Ingestion module — responsible for the **ETL (Extract, Transform, Load)** phase of the RAG pipeline.
Reads PDF documents from the Knowledge Base, processes them, and stores the resulting vector embeddings
in a pgvector database.

---

## Responsibilities

- Extract text from PDF documents using Apache Tika
- Transform and split documents into chunks
- Enrich document metadata
- Store embeddings in pgvector (PostgreSQL)
- Provide a search endpoint for validating Knowledge Base content

---

## Technology Stack

- **Java 25**, **Spring Boot 4**, **Spring AI 2.0.0-M5**
- **Embedding model**: OpenAI `text-embedding-3-small`
- **Vector database**: pgvector via Docker
- **Document reader**: Apache Tika
- **Observability**: OpenTelemetry + Grafana

---

## Port & Context Path

```
http://localhost:8081/rag-ingestor
```

---

## Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| `POST` | `/knowledge-base/reload` | Reload all documents into the vector database |
| `GET` | `/knowledge-base/search?query=` | Search the Knowledge Base for a given query |

### Examples

```bash
# Reload the Knowledge Base
POST http://localhost:8081/rag-ingestor/knowledge-base/reload

# Search
GET http://localhost:8081/rag-ingestor/knowledge-base/search?query=What is Movie Machine Pro?
```

---

## Configuration Profiles

| Profile | Description |
|---------|-------------|
| `groq-chat-openai-embed` | Groq LLM + OpenAI embeddings (default) |
| `groq-chat-ollama-embed` | Groq LLM + Ollama embeddings |
| `openai-chat-openai-embed` | OpenAI LLM + OpenAI embeddings |
| `ollama-chat-ollama-embed` | Ollama LLM + Ollama embeddings |

Active profile is set in `application.properties`:
```properties
spring.profiles.active=groq-chat-openai-embed
```

---

## Key Configuration Parameters

```properties
# Knowledge Base documents location
app.knowledgebase.path=classpath:knowledge_base/**/*

# ETL Text Splitter
app.etl.textsplitter.chunksize=512
app.etl.textsplitter.minchunksizechars=10
app.etl.textsplitter.minchunklengthtoembed=10
app.etl.textsplitter.maxnumchunks=5000
app.etl.textsplitter.keepseparator=true
```

---

## Infrastructure (Docker)

Started automatically by Spring Boot Docker Compose integration when the application starts.

```
docker/
└── docker-compose.yaml    # pgvector + Grafana (LGTM)
```

| Service | Port | URL |
|---------|------|-----|
| pgvector (PostgreSQL) | 5432 | |
| Grafana | 3000 | http://localhost:3000 |
| OTLP HTTP | 4318 | |
| OTLP gRPC | 4317 | |

---

## Package Structure

```
io.jprime.agenticrag.ingestor
├── web/                        # REST controllers
├── domain/
│   ├── config/                 # AI model configurations
│   ├── etl/
│   │   ├── reader/             # Document reading (Apache Tika)
│   │   ├── transformer/        # Document transformation and splitting
│   │   └── writer/             # Vector store writing
│   ├── service/                # KnowledgeBaseService
│   └── observability/          # OpenTelemetry configuration
└── persistence/                # VectorRepository
```
