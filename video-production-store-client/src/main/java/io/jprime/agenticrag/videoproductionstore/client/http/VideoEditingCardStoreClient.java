package io.jprime.agenticrag.videoproductionstore.client.http;

import io.jprime.agenticrag.videoproductionstore.client.dto.VideoEditingCardDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class VideoEditingCardStoreClient {

    private static final String BASE_PATH = "/video-editing-cards";

    private final RestClient restClient;

    public VideoEditingCardStoreClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /* Returns all records without pagination.
     Acceptable for workshop use with a fixed small dataset.
     For production use, add page/size parameters.
     */
    public List<VideoEditingCardDto> findAll() {
        return restClient.get()
                .uri(BASE_PATH)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Optional<VideoEditingCardDto> findById(Integer id) {
        ResponseEntity<VideoEditingCardDto> response = restClient.get()
                .uri(BASE_PATH + "/{id}", id)
                .retrieve()
                .onStatus(s -> s.value() == HttpStatus.NOT_FOUND.value(), (request, resp) -> resp.getBody().transferTo(OutputStream.nullOutputStream()))
                .toEntity(VideoEditingCardDto.class);
        return Optional.ofNullable(response.getBody());
    }

    public List<VideoEditingCardDto> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("minPrice and maxPrice cannot be null.");
        }

        if (minPrice.compareTo(BigDecimal.ZERO) < 0 || maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("minPrice and maxPrice cannot be negative.");
        }

        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("minPrice cannot be greater than maxPrice.");
        }

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/price")
                        .queryParam("minPrice", minPrice)
                        .queryParam("maxPrice", maxPrice)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Optional<VideoEditingCardDto> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Video Editing Card name cannot be blank.");
        }

        if (name.length() > 255) {
            throw new IllegalArgumentException("Video Editing Card name cannot exceed 255 characters.");
        }

        ResponseEntity<VideoEditingCardDto> response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/name")
                        .queryParam("name", name)
                        .build())
                .retrieve()
                .onStatus(s -> s.value() == HttpStatus.NOT_FOUND.value(), (request, resp) -> resp.getBody().transferTo(OutputStream.nullOutputStream()))
                .toEntity(VideoEditingCardDto.class);
        return Optional.ofNullable(response.getBody());
    }
}
