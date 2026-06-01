package io.jprime.agenticrag.retriever.domain.llm.mcp;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Duration;

/**
 * Manages the lifecycle of the MCP client connection to {@code video-production-store-mcp-server}.
 * Responsible for initializing the connection on startup and closing it on shutdown.
 * Active only when {@code app.tools.mode=mcp}.
 * <p>
 * The client is constructed programmatically via {@link McpClient#sync(Object)} with
 * {@link HttpClientSseClientTransport} — the SSE-based transport used by the MCP server.
 * Spring AI's MCP auto-configuration is intentionally bypassed here to allow explicit
 * control over transport, timeout, and client identity.
 */
@Component
@ConditionalOnProperty(name = "app.tools.mode", havingValue = "mcp")
public class McpConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(McpConnectionManager.class);

    @Value("${app.mcp.server.video-production-store-mcp-server.name}")
    private String mcpServerName;

    @Value("${app.mcp.server.video-production-store-mcp-server.url}")
    private String mcpServerUrl;

    @Value("${app.mcp.client.name}")
    private String mcpClientName;

    @Value("${app.mcp.client.version}")
    private String mcpClientVersion;

    @Value("${app.mcp.client.request-timeout}")
    private Long mcpRequestTimeout;

    private McpSyncClient mcpClient;

    /**
     * Initializes the {@link McpSyncClient} and performs the MCP handshake at startup.
     * Validates the server URL scheme before attempting the connection.
     * Throws if initialization fails — preventing the application from starting
     * in MCP mode without a reachable MCP server.
     */
    @PostConstruct
    void initMcpClient() {
        validateMcpServerUrl(mcpServerUrl);

        HttpClientSseClientTransport transport = HttpClientSseClientTransport
                .builder(mcpServerUrl)
                .build();

        mcpClient = McpClient.sync(transport)
                .clientInfo(new McpSchema.Implementation(mcpClientName, mcpClientVersion))
                .requestTimeout(Duration.ofSeconds(mcpRequestTimeout))
                .build();

        try {
            mcpClient.initialize();
            log.info("MCP client connection initialized successfully to: {}", mcpServerName);
        } catch (Exception e) {
            log.error("Failed to initialize MCP client connection to: {}", mcpServerName, e);
            throw e;
        }
    }

    /**
     * Validates that the MCP server URL has a valid {@code http} or {@code https} scheme.
     * Fails fast with a clear message rather than letting the transport layer produce
     * a cryptic connection error.
     */
    private void validateMcpServerUrl(String url) {
        try {
            URI uri = URI.create(url);
            String scheme = uri.getScheme();

            if (scheme == null || (!scheme.equals("http") && !scheme.equals("https"))) {
                throw new IllegalStateException(
                        "MCP server URL has an invalid scheme '" + scheme + "'. Only http and https are allowed.");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("MCP server URL is not a valid URI: " + url, e);
        }
    }

    /**
     * Creates a {@link ToolCallbackProvider} wrapping the active {@link McpSyncClient}.
     * Called by {@link io.jprime.agenticrag.retriever.domain.llm.config.ChatClientConfiguration}
     * when building the {@code agenticChatClient} bean in MCP mode.
     * The agent uses this provider to discover and invoke MCP tools autonomously.
     */
    public ToolCallbackProvider getToolCallbackProvider() {
        log.info("[Tool:mcp] Building ToolCallbackProvider for MCP server: {}", mcpServerName);
        return new SyncMcpToolCallbackProvider(mcpClient);
    }

    /**
     * Closes the {@link McpSyncClient} connection gracefully on application shutdown.
     */
    @PreDestroy
    public void closeMcpClient() {
        if (mcpClient != null) {
            log.info("Closing MCP client connection to: {}", mcpServerName);

            try {
                mcpClient.close();
            } catch (Exception e) {
                log.warn("Error while closing MCP client connection to: {}", mcpServerName, e);
            }
        }
    }
}
