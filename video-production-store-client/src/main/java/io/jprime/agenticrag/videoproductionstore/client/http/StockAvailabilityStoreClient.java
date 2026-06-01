package io.jprime.agenticrag.videoproductionstore.client.http;

import io.jprime.agenticrag.videoproductionstore.client.dto.StockAvailabilityDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public class StockAvailabilityStoreClient {

    private static final String BASE_PATH = "/stock-availabilities";

    private final RestClient restClient;

    public StockAvailabilityStoreClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /* Returns all records without pagination.
     Acceptable for workshop use with a fixed small dataset.
     For production use, add page/size parameters.
     */
    public List<StockAvailabilityDto> findAll() {
        return restClient.get()
                .uri(BASE_PATH)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Optional<StockAvailabilityDto> findByVideoCardId(Integer videoCardId) {
        ResponseEntity<StockAvailabilityDto> response = restClient.get()
                .uri(BASE_PATH + "/video-card/{videoCardId}", videoCardId)
                .retrieve()
                .onStatus(s -> s.value() == HttpStatus.NOT_FOUND.value(), (request, resp) -> resp.getBody().transferTo(OutputStream.nullOutputStream()))
                .toEntity(StockAvailabilityDto.class);
        return Optional.ofNullable(response.getBody());
    }

    public List<StockAvailabilityDto> findByMinQuantity(int minQuantity) {
        if (minQuantity < 0) {
            throw new IllegalArgumentException("Stock availability minQuantity cannot be negative.");
        }

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/min-quantity")
                        .queryParam("minQuantity", minQuantity)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
