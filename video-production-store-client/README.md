# video-production-store-client

Pre-built REST client library for `video-production-store`.
Encapsulates all REST communication logic and can be embedded as a dependency
in any Java project that needs to integrate with `video-production-store`.

> **Note:** This module is pre-built and complete. It is not modified during the workshop.

---

## Usage

Used as a library dependency in:
- `rag-retriever` — in Step 6 for distributed Agentic RAG via REST client
- `video-production-store-mcp-server` — internally for MCP tool implementations

---

## Dependency

```xml
<dependency>
    <groupId>io.jprime.agenticrag</groupId>
    <artifactId>video-production-store-client</artifactId>
    <version>${revision}</version>
</dependency>
```

---

## Architecture

```
video-production-store-client [library jar, RestClient]
    └── video-production-store [port 8080, REST + MySQL]
```
