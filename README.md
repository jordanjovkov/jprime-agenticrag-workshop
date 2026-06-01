# Implementing Agentic RAG using Spring AI
### JPrime 2026 — Workshop

A step-by-step implementation of an Agentic RAG system using Java, Spring Boot 4, and Spring AI 2.
The system evolves incrementally from a simple LLM client to a fully functional Agentic RAG capable
of autonomously deciding which data sources to query and how to combine the results.

---

## Project Structure

```
agenticrag-workshop
├── rag-ingestor                      # RAG Ingestion module — ETL pipeline → pgvector (port 8081)
├── rag-retriever                     # RAG Retrieval module — LLM / Naive RAG / Agentic RAG (port 8082)
├── video-production-store            # Pre-built: REST API + MySQL (port 8080)
├── video-production-store-client     # Pre-built: REST client library
└── video-production-store-mcp-server # Pre-built: MCP server (port 8083)
```

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java | 25 |
| Maven | 3.9+ |
| Docker Desktop | latest |

### API Keys (Environment Variables)

```bash
GROQ_API_KEY=<your-groq-api-key>        # Free tier: https://console.groq.com
OPENAI_API_KEY=<provided-by-instructor>  # For text-embedding-3-small embedding model
```

---

## Workshop Steps

Each step is isolated in a separate Git branch with a fully working version of the project.

| Branch | Step | Description |
|--------|------|-------------|
| `workshop-v1-initial` | Step 1 | Initial project structure + observability |
| `workshop-v2-llm` | Step 2 | Direct LLM communication |
| `workshop-v3-knowledge-base` | Step 3 | Knowledge Base (ETL + pgvector) |
| `workshop-v4-naive-rag` | Step 4 | Naive RAG |
| `workshop-v5-agentic-rag` | Step 5 | Agentic RAG (local, in-memory data) |
| `workshop-v6-agentic-rag-rest-client` | Step 6 | Agentic RAG (distributed, REST client) |
| `workshop-v7-agentic-rag-mcp-client` | Step 7 | Agentic RAG (distributed, MCP server) |

---

## Starting the Project

### 1. Start pre-built services

```bash
# Video Production Store (port 8080) — starts MySQL via Docker Compose
cd video-production-store
mvn spring-boot:run

# Video Production Store MCP Server (port 8083)
cd video-production-store-mcp-server
mvn spring-boot:run
```

### 2. Start RAG modules

```bash
# RAG Ingestor (port 8081) — starts pgvector + Grafana via Docker Compose
cd rag-ingestor
mvn spring-boot:run

# RAG Retriever (port 8082) — reuses existing Docker containers
cd rag-retriever
mvn spring-boot:run
```

### 3. Load the Knowledge Base

```bash
POST http://localhost:8081/rag-ingestor/knowledge-base/reload
```

---

## Key URLs

| Service | URL |
|---------|-----|
| Grafana Observability Dashboard | http://localhost:3000 |
| Video Production Store Swagger | http://localhost:8080/video-production-store/swagger-ui.html |
| MCP Inspector | `npx @modelcontextprotocol/inspector http://localhost:8083` |

---

## Technology Stack

- **Java 25**, **Spring Boot 4**, **Spring AI 2.0.0-M5**
- **LLM**: `openai/gpt-oss-120b` via Groq free tier
- **Embedding model**: OpenAI `text-embedding-3-small`
- **Vector database**: pgvector (PostgreSQL)
- **Relational database**: MySQL
- **Observability**: OpenTelemetry + Grafana (LGTM stack)
- **MCP**: Model Context Protocol (SSE transport)
