package io.jprime.agenticrag.retriever.persistence.videoproductionstore.inmemoryimpl;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.StockAvailability;
import io.jprime.agenticrag.retriever.persistence.videoproductionstore.StockAvailabilityRepository;
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
public class InMemoryStockAvailabilityRepository implements StockAvailabilityRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryStockAvailabilityRepository.class);

    private static final String OBSERVATION_NAME = "repository.videoproductionstore";
    private static final String ENTITY = "StockAvailability";

    private final List<StockAvailability> stockAvailabilities = InMemoryDataset.STOCK_AVAILABILITIES;
    private final ObservationRegistry observationRegistry;

    public InMemoryStockAvailabilityRepository(ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;
    }

    @Override
    public List<StockAvailability> findAll() {
        log.info("[Repository:memory] StockAvailability.findAll");

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findAll")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> stockAvailabilities);
    }

    @Override
    public Optional<StockAvailability> findByVideoCardId(Integer videoCardId) {
        log.info("[Repository:memory] StockAvailability.findByVideoCardId — videoCardId: {}", videoCardId);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByVideoCardId")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> stockAvailabilities.stream()
                        .filter(s -> s.videoEditingCard().id().equals(videoCardId))
                        .findFirst());
    }

    @Override
    public List<StockAvailability> findByMinQuantity(int minQuantity) {
        log.info("[Repository:memory] StockAvailability.findByMinQuantity — minQuantity: {}", minQuantity);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByMinQuantity")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> stockAvailabilities.stream()
                        .filter(s -> s.availability() >= minQuantity)
                        .toList());
    }
}
