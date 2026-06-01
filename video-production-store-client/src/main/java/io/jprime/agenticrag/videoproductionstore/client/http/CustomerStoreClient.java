package io.jprime.agenticrag.videoproductionstore.client.http;

import io.jprime.agenticrag.videoproductionstore.client.dto.CustomerDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public class CustomerStoreClient {

    private static final String BASE_PATH = "/customers";

    private final RestClient restClient;

    public CustomerStoreClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /* Returns all records without pagination.
     Acceptable for workshop use with a fixed small dataset.
     For production use, add page/size parameters.
     */
    public List<CustomerDto> findAll() {
        return restClient.get()
                .uri(BASE_PATH)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Optional<CustomerDto> findById(Integer id) {
        ResponseEntity<CustomerDto> response = restClient.get()
                .uri(BASE_PATH + "/{id}", id)
                .retrieve()
                .onStatus(s -> s.value() == HttpStatus.NOT_FOUND.value(), (request, resp) -> resp.getBody().transferTo(OutputStream.nullOutputStream()))
                .toEntity(CustomerDto.class);
        return Optional.ofNullable(response.getBody());
    }

    public List<CustomerDto> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer Name cannot be blank.");
        }

        if (name.length() > 255) {
            throw new IllegalArgumentException("Customer Name cannot exceed 255 characters.");
        }

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/name")
                        .queryParam("name", name)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<CustomerDto> findByVideoCardId(Integer videoCardId) {
        return restClient.get()
                .uri(BASE_PATH + "/video-card/{videoCardId}", videoCardId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
