package io.jprime.agenticrag.retriever.persistence.videoproductionstore.httpclientimpl;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Customer;
import io.jprime.agenticrag.retriever.persistence.videoproductionstore.CustomerRepository;
import io.jprime.agenticrag.videoproductionstore.client.dto.CustomerDto;
import io.jprime.agenticrag.videoproductionstore.client.http.CustomerStoreClient;
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
public class StoreHttpClientCustomerRepository implements CustomerRepository {

    private static final Logger log = LoggerFactory.getLogger(StoreHttpClientCustomerRepository.class);

    private static final String OBSERVATION_NAME = "repository.videoproductionstore";
    private static final String ENTITY = "Customer";

    private final CustomerStoreClient client;
    private final ObservationRegistry observationRegistry;

    public StoreHttpClientCustomerRepository(CustomerStoreClient client,
                                             ObservationRegistry observationRegistry) {
        this.client = client;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public List<Customer> findAll() {
        log.info("[Repository:http-client] Customer.findAll");

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findAll")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findAll().stream()
                        .map(this::toModel)
                        .toList());
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        log.info("[Repository:http-client] Customer.findById — id: {}", id);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findById")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findById(id)
                        .map(this::toModel));
    }

    @Override
    public List<Customer> findByName(String name) {
        log.info("[Repository:http-client] Customer.findByName — name: '{}'", name);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByName")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findByName(name).stream()
                        .map(this::toModel)
                        .toList());
    }

    @Override
    public List<Customer> findByVideoCardId(Integer videoCardId) {
        log.info("[Repository:http-client] Customer.findByVideoCardId — videoCardId: {}", videoCardId);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByVideoCardId")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findByVideoCardId(videoCardId).stream()
                        .map(this::toModel)
                        .toList());
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
}
