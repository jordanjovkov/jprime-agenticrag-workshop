package io.jprime.agenticrag.videoproductionstore.client.config;

import io.jprime.agenticrag.videoproductionstore.client.http.CustomerStoreClient;
import io.jprime.agenticrag.videoproductionstore.client.http.OrderStoreClient;
import io.jprime.agenticrag.videoproductionstore.client.http.StockAvailabilityStoreClient;
import io.jprime.agenticrag.videoproductionstore.client.http.VideoEditingCardStoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.http.HttpClient;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

/**
 * Configures the shared {@link RestClient} and all store client beans
 * used to communicate with the {@code video-production-store} REST API.
 * <p>
 * The {@link RestClient} is built from the Spring Boot auto-configured
 * {@link RestClient.Builder} — injecting the builder (rather than calling
 * {@code RestClient.builder()} statically) is required for Spring Boot's
 * observability instrumentation to register HTTP-level spans visible in Jaeger.
 * <p>
 * Timeouts are configured explicitly via {@link JdkClientHttpRequestFactory}:
 * <ul>
 *   <li>{@code connectionTimeout} — {@value} seconds: time to establish the TCP connection</li>
 *   <li>{@code readTimeout} — {@value} seconds: time to wait for a response after the request is sent</li>
 * </ul>
 * A centralized error handler converts all 4xx and 5xx responses into
 * {@link RestClientResponseException}, preserving the original status code,
 * headers, and response body for upstream error handling.
 */
@Configuration
public class VideoProductionStoreClientConfig {

    private static final Duration REST_CLIENT_CONNECTION_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration REST_CLIENT_READ_TIMEOUT = Duration.ofSeconds(30);

    @Bean
    public RestClient videoProductionStoreRestClient(
            RestClient.Builder restClientBuilder,
            @Value("${app.video-production-store.base-url}") String baseUrl) {

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(REST_CLIENT_CONNECTION_TIMEOUT)
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(REST_CLIENT_READ_TIMEOUT);

        return restClientBuilder
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    byte[] responseBody = response.getBody().readAllBytes();
                    Charset charset = Optional.ofNullable(response.getHeaders().getContentType())
                            .map(MediaType::getCharset)
                            .orElse(StandardCharsets.UTF_8);

                    throw new RestClientResponseException(
                            "Video Production Store error: " + response.getStatusCode(),
                            response.getStatusCode(),
                            response.getStatusText(),
                            response.getHeaders(),
                            responseBody,
                            charset);
                })
                .build();
    }

    @Bean
    public VideoEditingCardStoreClient videoEditingCardClient(RestClient videoProductionStoreRestClient) {
        return new VideoEditingCardStoreClient(videoProductionStoreRestClient);
    }

    @Bean
    public CustomerStoreClient customerClient(RestClient videoProductionStoreRestClient) {
        return new CustomerStoreClient(videoProductionStoreRestClient);
    }

    @Bean
    public StockAvailabilityStoreClient stockAvailabilityClient(RestClient videoProductionStoreRestClient) {
        return new StockAvailabilityStoreClient(videoProductionStoreRestClient);
    }

    @Bean
    public OrderStoreClient orderClient(RestClient videoProductionStoreRestClient) {
        return new OrderStoreClient(videoProductionStoreRestClient);
    }
}
