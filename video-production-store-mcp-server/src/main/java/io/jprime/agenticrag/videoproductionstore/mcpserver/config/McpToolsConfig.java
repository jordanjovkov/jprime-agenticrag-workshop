package io.jprime.agenticrag.videoproductionstore.mcpserver.config;

import io.jprime.agenticrag.videoproductionstore.mcpserver.tool.CustomerTools;
import io.jprime.agenticrag.videoproductionstore.mcpserver.tool.OrderTools;
import io.jprime.agenticrag.videoproductionstore.mcpserver.tool.StockAvailabilityTools;
import io.jprime.agenticrag.videoproductionstore.mcpserver.tool.VideoEditingCardTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers the MCP tool beans exposed by this server to connecting MCP clients.
 * <p>
 * Each {@link ToolCallbackProvider} bean wraps a {@code @Service} tool class via
 * {@link MethodToolCallbackProvider}, which inspects the class for {@code @Tool}-annotated
 * methods and registers them as callable MCP tools.
 * <p>
 * When an MCP client connects (e.g. {@code McpConnectionManager} in {@code rag-retriever}),
 * it performs a tool discovery handshake and receives the full list of available tools
 * with their descriptions and parameter schemas — without any knowledge of the
 * server's internal implementation.
 */
@Configuration
public class McpToolsConfig {

    @Bean
    public ToolCallbackProvider videoEditingCardToolCallbackProvider(VideoEditingCardTools videoEditingCardTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(videoEditingCardTools)
                .build();
    }

    @Bean
    public ToolCallbackProvider customerToolCallbackProvider(CustomerTools customerTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(customerTools)
                .build();
    }

    @Bean
    public ToolCallbackProvider stockAvailabilityToolCallbackProvider(StockAvailabilityTools stockAvailabilityTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(stockAvailabilityTools)
                .build();
    }

    @Bean
    public ToolCallbackProvider orderToolCallbackProvider(OrderTools orderTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(orderTools)
                .build();
    }
}
