package io.jprime.agenticrag.videoproductionstore.client.http;

import io.jprime.agenticrag.videoproductionstore.client.dto.OrderDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public class OrderStoreClient {

    private static final String BASE_PATH = "/orders";

    private final RestClient restClient;

    public OrderStoreClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /* Returns all records without pagination.
     Acceptable for workshop use with a fixed small dataset.
     For production use, add page/size parameters.
     */
    public List<OrderDto> findAll() {
        return restClient.get()
                .uri(BASE_PATH)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Optional<OrderDto> findById(Integer id) {
        ResponseEntity<OrderDto> response = restClient.get()
                .uri(BASE_PATH + "/{id}", id)
                .retrieve()
                .onStatus(s -> s.value() == HttpStatus.NOT_FOUND.value(), (request, resp) -> resp.getBody().transferTo(OutputStream.nullOutputStream()))
                .toEntity(OrderDto.class);
        return Optional.ofNullable(response.getBody());
    }

    public List<OrderDto> findByCustomerId(Integer customerId) {
        return restClient.get()
                .uri(BASE_PATH + "/customer/{customerId}", customerId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
