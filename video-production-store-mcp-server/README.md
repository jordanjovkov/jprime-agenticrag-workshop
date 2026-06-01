# video-production-store-mcp-server

Pre-built MCP (Model Context Protocol) server that exposes the functionality of
`video-production-store` via the MCP protocol. Used as an external tool provider
by `rag-retriever` in Step 7 of the workshop.

> **Note:** This module is pre-built and complete. It is not modified during the workshop.
> It is treated as a black-box MCP Server — the agent interacts with it without any
> knowledge of its internal implementation.

---

## Responsibilities

Exposes the same data access capabilities as the `video-production-store` REST API,
but through a standardized MCP protocol that allows AI agents to discover and invoke
its tools autonomously.

---

## Technology Stack

- **Java 25**, **Spring Boot 4**, **Spring AI 2.0.0-M5**
- **Protocol**: MCP (Model Context Protocol) via SSE transport
- **Internal communication**: `video-production-store-client` REST library

---

## Port

```
http://localhost:8083
```

---

## MCP Inspector

Use the MCP Inspector to explore available tools:

```bash
npx @modelcontextprotocol/inspector http://localhost:8083
```

---

## Architecture

```
video-production-store-mcp-server [port 8083, MCP/SSE]
    └── video-production-store-client [library jar, RestClient]
            └── video-production-store [port 8080, REST + MySQL]
```
