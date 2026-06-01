package io.jprime.agenticrag.videoproductionstore.mcpserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures CORS policy for the MCP server via Spring profiles.
 * <p>
 * Two mutually exclusive profiles are available:
 * <ul>
 *   <li>{@code cors-allow-all} — allows all origins, methods and headers.
 *       Intended for local development with MCP Inspector.
 *       <strong>Never activate in shared or production environments.</strong></li>
 *   <li>{@code cors-allow-only-local} — restricts access to known local origins only:
 *       {@code rag-retriever} (port 8082), MCP Inspector UI (port 6274),
 *       and MCP Inspector proxy (port 6277).</li>
 * </ul>
 * If neither profile is active, no CORS configuration is applied.
 */
@Configuration
public class WebConfig {

    private static final String RAG_RETRIEVER_ORIGIN        = "http://localhost:8082";
    private static final String MCP_INSPECTOR_UI_ORIGIN     = "http://localhost:6274";
    private static final String MCP_INSPECTOR_PROXY_ORIGIN  = "http://localhost:6277";

    /**
     * WARNING: This CORS configuration allows all origins, methods and headers.
     * For local development with MCP Inspector only.
     * NEVER activate this profile in shared or production environments.
     */
    @Bean
    @Profile("cors-allow-all")
    public WebMvcConfigurer permissiveCorsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    @Profile("cors-allow-only-local")
    public WebMvcConfigurer restrictedCorsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                RAG_RETRIEVER_ORIGIN,
                                MCP_INSPECTOR_UI_ORIGIN,
                                MCP_INSPECTOR_PROXY_ORIGIN)
                        .allowedMethods("GET", "POST")
                        .allowedHeaders("Content-Type", "Authorization");
            }
        };
    }
}
