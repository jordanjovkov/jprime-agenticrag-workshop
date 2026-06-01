package io.jprime.agenticrag.videoproductionstore.client.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

abstract class AbstractClientIntegrationTest {

    protected static final VideoEditingCardStoreClient VIDEO_EDITING_CARD_STORE_CLIENT;
    protected static final CustomerStoreClient CUSTOMER_STORE_CLIENT;
    protected static final StockAvailabilityStoreClient STOCK_AVAILABILITY_STORE_CLIENT;
    protected static final OrderStoreClient ORDER_STORE_CLIENT;

    static {
        String baseUrl = loadBaseUrl();

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);

        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .messageConverters(converters -> {
                    converters.clear();
                    converters.add(converter);
                })
                .build();

        VIDEO_EDITING_CARD_STORE_CLIENT = new VideoEditingCardStoreClient(restClient);
        CUSTOMER_STORE_CLIENT = new CustomerStoreClient(restClient);
        STOCK_AVAILABILITY_STORE_CLIENT = new StockAvailabilityStoreClient(restClient);
        ORDER_STORE_CLIENT = new OrderStoreClient(restClient);
    }

    private static String loadBaseUrl() {
        Properties properties = new Properties();
        try (InputStream input = AbstractClientIntegrationTest.class
                .getClassLoader()
                .getResourceAsStream("test-client.properties")) {
            if (input == null) {
                throw new IllegalStateException("test-client.properties not found in test resources");
            }
            properties.load(input);

            String url = properties.getProperty("app.video-production-store.base-url");

            if (url == null || url.isBlank()) {
                throw new IllegalStateException(
                        "Property 'app.video-production-store.base-url' is missing from test-client.properties.");
            }

            return url;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load test-client.properties", e);
        }
    }
}
