package io.jprime.agenticrag.retriever.persistence.videoproductionstore.httpclientimpl;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.StockAvailability;
import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.VideoEditingCard;
import io.jprime.agenticrag.retriever.persistence.videoproductionstore.StockAvailabilityRepository;
import io.jprime.agenticrag.videoproductionstore.client.dto.StockAvailabilityDto;
import io.jprime.agenticrag.videoproductionstore.client.dto.VideoEditingCardDto;
import io.jprime.agenticrag.videoproductionstore.client.http.StockAvailabilityStoreClient;
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
public class StoreHttpClientStockAvailabilityRepository implements StockAvailabilityRepository {

    private static final Logger log = LoggerFactory.getLogger(StoreHttpClientStockAvailabilityRepository.class);

    private static final String OBSERVATION_NAME = "repository.videoproductionstore";
    private static final String ENTITY = "StockAvailability";

    private final StockAvailabilityStoreClient client;
    private final ObservationRegistry observationRegistry;

    public StoreHttpClientStockAvailabilityRepository(StockAvailabilityStoreClient client,
                                                      ObservationRegistry observationRegistry) {
        this.client = client;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public List<StockAvailability> findAll() {
        log.info("[Repository:http-client] StockAvailability.findAll");

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findAll")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findAll().stream()
                        .map(this::toModel)
                        .toList());
    }

    @Override
    public Optional<StockAvailability> findByVideoCardId(Integer videoCardId) {
        log.info("[Repository:http-client] StockAvailability.findByVideoCardId — videoCardId: {}", videoCardId);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByVideoCardId")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findByVideoCardId(videoCardId)
                        .map(this::toModel));
    }

    @Override
    public List<StockAvailability> findByMinQuantity(int minQuantity) {
        log.info("[Repository:http-client] StockAvailability.findByMinQuantity — minQuantity: {}", minQuantity);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByMinQuantity")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findByMinQuantity(minQuantity).stream()
                        .map(this::toModel)
                        .toList());
    }

    private StockAvailability toModel(StockAvailabilityDto dto) {
        return new StockAvailability(
                dto.id(),
                toModel(dto.videoEditingCard()),
                dto.availability()
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
