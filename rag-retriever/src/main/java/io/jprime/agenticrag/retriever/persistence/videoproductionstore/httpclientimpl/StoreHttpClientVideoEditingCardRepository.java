package io.jprime.agenticrag.retriever.persistence.videoproductionstore.httpclientimpl;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.VideoEditingCard;
import io.jprime.agenticrag.retriever.persistence.videoproductionstore.VideoEditingCardRepository;
import io.jprime.agenticrag.videoproductionstore.client.dto.VideoEditingCardDto;
import io.jprime.agenticrag.videoproductionstore.client.http.VideoEditingCardStoreClient;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "client")
public class StoreHttpClientVideoEditingCardRepository implements VideoEditingCardRepository {

    private static final Logger log = LoggerFactory.getLogger(StoreHttpClientVideoEditingCardRepository.class);

    private static final String OBSERVATION_NAME = "repository.videoproductionstore";
    private static final String ENTITY = "VideoEditingCard";

    private final VideoEditingCardStoreClient client;
    private final ObservationRegistry observationRegistry;

    public StoreHttpClientVideoEditingCardRepository(VideoEditingCardStoreClient client,
                                                     ObservationRegistry observationRegistry) {
        this.client = client;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public List<VideoEditingCard> findAll() {
        log.info("[Repository:http-client] VideoEditingCard.findAll");

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findAll")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findAll().stream()
                        .map(this::toModel)
                        .toList());
    }

    @Override
    public Optional<VideoEditingCard> findById(Integer id) {
        log.info("[Repository:http-client] VideoEditingCard.findById — id: {}", id);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findById")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findById(id)
                        .map(this::toModel));
    }

    @Override
    public List<VideoEditingCard> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("[Repository:http-client] VideoEditingCard.findByPriceRange — minPrice: {}, maxPrice: {}",
                minPrice, maxPrice);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByPriceRange")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findByPriceRange(minPrice, maxPrice).stream()
                        .map(this::toModel)
                        .toList());
    }

    @Override
    public Optional<VideoEditingCard> findByName(String name) {
        log.info("[Repository:http-client] VideoEditingCard.findByName — name: '{}'", name);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByName")
                .lowCardinalityKeyValue("type", "http-client")
                .observe(() -> client.findByName(name)
                        .map(this::toModel));
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
