package io.jprime.agenticrag.retriever.persistence.videoproductionstore.httpclientimpl;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Customer;
import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Order;
import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.VideoEditingCard;
import io.jprime.agenticrag.retriever.persistence.videoproductionstore.OrderRepository;
import io.jprime.agenticrag.videoproductionstore.client.dto.CustomerDto;
import io.jprime.agenticrag.videoproductionstore.client.dto.OrderDto;
import io.jprime.agenticrag.videoproductionstore.client.dto.VideoEditingCardDto;
import io.jprime.agenticrag.videoproductionstore.client.http.OrderStoreClient;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "client")
public class StoreHttpClientOrderRepository implements OrderRepository {

    private static final Logger log = LoggerFactory.getLogger(StoreHttpClientOrderRepository.class);

    private static final String OBSERVATION_NAME = "repository.videoproductionstore";
    private static final String ENTITY = "Order";

    private final OrderStoreClient client;
    private final ObservationRegistry observationRegistry;

    public StoreHttpClientOrderRepository(OrderStoreClient client,
                                          ObservationRegistry observationRegistry) {
        this.client = client;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public List<Order> findAll() {
        log.info("[Repository:http-client] Order.findAll");

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findAll")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findAll().stream()
                        .map(this::toModel)
                        .toList());
    }

    @Override
    public Optional<Order> findById(Integer id) {
        log.info("[Repository:http-client] Order.findById — id: {}", id);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findById")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findById(id)
                        .map(this::toModel));
    }

    @Override
    public List<Order> findByCustomerId(Integer customerId) {
        log.info("[Repository:http-client] Order.findByCustomerId — customerId: {}", customerId);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByCustomerId")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findByCustomerId(customerId).stream()
                        .map(this::toModel)
                        .toList());
    }

    private Order toModel(OrderDto dto) {
        return new Order(
                dto.id(),
                toModel(dto.customer()),
                toModel(dto.videoEditingCard()),
                dto.orderDate(),
                dto.orderNote()
        );
    }

    private Customer toModel(CustomerDto dto) {
        return new Customer(
                dto.id(),
                dto.name(),
                dto.email(),
                dto.phone(),
                dto.address(),
                dto.notes()
        );
    }

    private VideoEditingCard toModel(VideoEditingCardDto dto) {
        return new VideoEditingCard(
                dto.id(),
                dto.name(),
                dto.manufacturer(),
                dto.description(),
                dto.price()
        );
    }
}
