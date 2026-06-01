# rag-retriever

RAG Retrieval module — the core project of the workshop. Evolves incrementally through all development
steps from a simple LLM client to a fully functional Agentic RAG capable of routing queries across
multiple data sources autonomously.

---

## Responsibilities

- Direct LLM communication
- Naive RAG queries using the Knowledge Base (pgvector)
- Agentic RAG with autonomous tool selection across multiple data sources:
  - Knowledge Base (pgvector)
  - Video Production Store (in-memory simulation or via REST client)
  - Video Production Store MCP Server

---

## Technology Stack

- **Java 25**, **Spring Boot 4**, **Spring AI 2.0.0-M5**
- **LLM**: `openai/gpt-oss-120b` via Groq free tier
- **Embedding model**: OpenAI `text-embedding-3-small`
- **Vector database**: pgvector via Docker
- **MCP Client**: Model Context Protocol (SSE transport)
- **Observability**: OpenTelemetry + Grafana

---

## Port & Context Path

```
http://localhost:8082/rag-retriever
```

---

## Endpoints

### Step 2 — Direct LLM

| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/llm/ask?prompt=` | Direct LLM query (sync) |
| `GET` | `/llm/ask-stream?prompt=` | Direct LLM query (streaming) |

### Step 4 — Naive RAG

| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/naive-rag/ask?prompt=` | Naive RAG query (sync) |
| `GET` | `/naive-rag/ask-stream?prompt=` | Naive RAG query (streaming) |
| `GET` | `/naive-rag/video-editing-cards` | Structured list of video editing cards from Knowledge Base |
| `GET` | `/naive-rag/video-editing-cards-multi-document` | Structured list of video editing cards using targeted per-document retrieval |

### Step 5–7 — Agentic RAG

| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/agentic-rag/ask?prompt=` | Agentic RAG query |

### Sample Requests

```bash
# Step 2 - Direct LLM
GET http://localhost:8082/rag-retriever/llm/ask?prompt=What is Movie Machine Pro?

# Step 4 - Naive RAG
GET http://localhost:8082/rag-retriever/naive-rag/ask?prompt=What is Media 100?
GET http://localhost:8082/rag-retriever/naive-rag/video-editing-cards
GET http://localhost:8082/rag-retriever/naive-rag/video-editing-cards-multi-document

# Step 5-7 - Agentic RAG
GET http://localhost:8082/rag-retriever/agentic-rag/ask?prompt=Is 386 processor enough for a computer for working with Movie Machine Pro?
```

---

## Configuration Profiles

| Profile | Description |
|---------|-------------|
| `groq-chat-openai-embed` | Groq LLM + OpenAI embeddings (default) |
| `groq-chat-ollama-embed` | Groq LLM + Ollama embeddings |
| `openai-chat-openai-embed` | OpenAI LLM + OpenAI embeddings |
| `ollama-chat-ollama-embed` | Ollama LLM + Ollama embeddings |

---

## Key Configuration Parameters

```properties
# -----------------------------------------------------------------
# Naive RAG
# -----------------------------------------------------------------

# Prompt template for standard Naive RAG queries
app.naiverag.prompttemplate=classpath:prompt_templates/naive_rag/prompt-template.txt

# Search parameters for standard Naive RAG queries
app.naiverag.searchrequest.topK=8
app.naiverag.searchrequest.similarityThreshold=0.5

# Video Editing Cards — Naive RAG (single-document, demonstrates RAG limitation)
app.naiverag.videoeditingcards.prompt=classpath:prompt_templates/naive_rag/video-editing-cards-prompt.txt

# Video Editing Cards — Multi-document RAG (solves single-document limitation)
app.naiverag.videoeditingcards.multidocument.prompt=classpath:prompt_templates/naive_rag/video-editing-cards-multi-document-prompt.txt
app.naiverag.videoeditingcards.multidocument.searchrequest.topK=2
app.naiverag.videoeditingcards.multidocument.searchrequest.similarityThreshold=0.3

# -----------------------------------------------------------------
# Agentic RAG
# -----------------------------------------------------------------

# Prompt template for Agentic RAG system prompt
app.agenticrag.systemprompttemplate=classpath:prompt_templates/agentic_rag/system-prompt-template.txt

# Search parameters for Agentic RAG Knowledge Base tool queries
app.agenticrag.searchrequest.topK=8
app.agenticrag.searchrequest.similarityThreshold=0.5

# -----------------------------------------------------------------
# External Services
# -----------------------------------------------------------------
app.video-production-store.base-url=http://localhost:8080/video-production-store/api

# -----------------------------------------------------------------
# Tools Configuration
# -----------------------------------------------------------------

# Tools Mode: local | mcp
app.tools.mode=local

# -----------------------------------------------------------------
# Repository Configuration
# -----------------------------------------------------------------

# Repository Type: memory | client
# When app.tools.mode=local, the repository can be in-memory or REST client
app.repository.type=memory

# -----------------------------------------------------------------
# MCP Server Connection
# -----------------------------------------------------------------
app.mcp.server.video-production-store-mcp-server.name=video-production-store-mcp-server
app.mcp.server.video-production-store-mcp-server.url=http://localhost:8083/sse

# -----------------------------------------------------------------
# MCP Client Configuration
# -----------------------------------------------------------------

# Requires Maven build (mvn package or mvn install) to resolve @project.version@
app.mcp.client.name=rag-retriever-mcp-client
app.mcp.client.version=@project.version@
app.mcp.client.request-timeout=30
```

### Tools Mode

| Value | Description | Workshop Step |
|-------|-------------|---------------|
| `local` | Uses local `@Tool` beans — in-memory or REST client data | Steps 5–6 |
| `mcp` | Uses MCP tools from `video-production-store-mcp-server` | Step 7 |

### Repository Type

| Value | Description | Workshop Step |
|-------|-------------|---------------|
| `memory` | In-memory dataset simulation | Step 5 |
| `client` | REST client to `video-production-store` | Steps 6–7 |

> **Note:** `app.repository.type` is only relevant when `app.tools.mode=local`.

---

## Infrastructure (Docker)

Started automatically by Spring Boot Docker Compose integration when the application starts.

```
docker/
└── docker-compose.yaml    # pgvector + Grafana (LGTM)
```

Shares the same Docker containers as `rag-ingestor` — if already running, they will be reused.

| Service | Port | URL |
|---------|------|-----|
| pgvector (PostgreSQL) | 5432 | |
| Grafana | 3000 | http://localhost:3000 |
| OTLP HTTP | 4318 | |
| OTLP gRPC | 4317 | |

---

## Package Structure

```
io.jprime.agenticrag.retriever
├── web/
│   ├── controller/             # LLMController, NaiveRAGController, AgenticRAGController
│   └── facade/                 # DirectLLMFacade, RAGChatFacade, VideoEditingCardsFacade
├── domain/
│   ├── llm/
│   │   ├── client/
│   │   │   ├── stream/         # Streaming chat clients
│   │   │   └── sync/           # Sync chat clients
│   │   ├── config/             # AI model configurations
│   │   ├── factory/            # ChatClientFactory, RAGAdvisorFactory
│   │   ├── service/            # PromptService
│   │   └── tool/               # Agent tools (KnowledgeBase, Customer, Order, VideoEditingCard)
│   ├── model/
│   │   ├── llmresponse/        # Typed LLM response objects
│   │   └── videoproductionstore/ # Domain model objects
│   ├── observability/          # OpenTelemetry configuration
│   └── service/                # Domain services (Customer, Order, VideoEditingCard, VideoEditingCards, KnowledgeBase)
└── persistence/
    ├── knowledgebase/          # KnowledgeBaseRepository + PgVector implementation
    └── videoproductionstore/
        ├── httpclientimpl/     # REST client implementations
        └── inmemoryimpl/       # In-memory implementations + InMemoryDataset
```
