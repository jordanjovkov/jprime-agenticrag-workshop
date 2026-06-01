package io.jprime.agenticrag.retriever.persistence.videoproductionstore.inmemoryimpl;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Order;
import io.jprime.agenticrag.retriever.persistence.videoproductionstore.OrderRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryOrderRepository implements OrderRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryOrderRepository.class);

    private static final String OBSERVATION_NAME = "repository.videoproductionstore";
    private static final String ENTITY = "Order";

    private final List<Order> orders = InMemoryDataset.ORDERS;
    private final ObservationRegistry observationRegistry;

    public InMemoryOrderRepository(ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;
    }

    @Override
    public List<Order> findAll() {
        log.info("[Repository:memory] Order.findAll");

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findAll")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> orders);
    }

    @Override
    public Optional<Order> findById(Integer id) {
        log.info("[Repository:memory] Order.findById — id: {}", id);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findById")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> orders.stream()
                        .filter(o -> o.id().equals(id))
                        .findFirst());
    }

    @Override
    public List<Order> findByCustomerId(Integer customerId) {
        log.info("[Repository:memory] Order.findByCustomerId — customerId: {}", customerId);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByCustomerId")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> orders.stream()
                        .filter(o -> o.customer().id().equals(customerId))
                        .toList());
    }
}
