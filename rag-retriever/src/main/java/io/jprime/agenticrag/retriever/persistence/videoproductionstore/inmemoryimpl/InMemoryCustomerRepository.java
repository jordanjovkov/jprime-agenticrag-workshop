package io.jprime.agenticrag.retriever.persistence.videoproductionstore.inmemoryimpl;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Customer;
import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Order;
import io.jprime.agenticrag.retriever.persistence.videoproductionstore.CustomerRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "memory", matchIfMissing = true)
public class InMemoryCustomerRepository implements CustomerRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryCustomerRepository.class);

    private static final String OBSERVATION_NAME = "repository.videoproductionstore";
    private static final String ENTITY = "Customer";

    private final List<Customer> customers = InMemoryDataset.CUSTOMERS;
    private final List<Order> orders = InMemoryDataset.ORDERS;
    private final ObservationRegistry observationRegistry;

    public InMemoryCustomerRepository(ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;
    }

    @Override
    public List<Customer> findAll() {
        log.info("[Repository:memory] Customer.findAll");

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findAll")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> customers);
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        log.info("[Repository:memory] Customer.findById — id: {}", id);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findById")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> customers.stream()
                        .filter(c -> c.id().equals(id))
                        .findFirst());
    }

    @Override
    public List<Customer> findByName(String name) {
        log.info("[Repository:memory] Customer.findByName — name: '{}'", name);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByName")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> customers.stream()
                        .filter(c -> c.name().toLowerCase().contains(name.toLowerCase()))
                        .toList());
    }

    @Override
    public List<Customer> findByVideoCardId(Integer videoCardId) {
        log.info("[Repository:memory] Customer.findByVideoCardId — videoCardId: {}", videoCardId);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByVideoCardId")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> orders.stream()
                        .filter(o -> o.videoEditingCard().id().equals(videoCardId))
                        .map(Order::customer)
                        .distinct()
                        .toList());
    }
}
