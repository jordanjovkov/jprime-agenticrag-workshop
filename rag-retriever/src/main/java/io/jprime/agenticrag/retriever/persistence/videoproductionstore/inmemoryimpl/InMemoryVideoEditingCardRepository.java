package io.jprime.agenticrag.retriever.persistence.videoproductionstore.inmemoryimpl;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.VideoEditingCard;
import io.jprime.agenticrag.retriever.persistence.videoproductionstore.VideoEditingCardRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryVideoEditingCardRepository implements VideoEditingCardRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryVideoEditingCardRepository.class);

    private static final String OBSERVATION_NAME = "repository.videoproductionstore";
    private static final String ENTITY = "VideoEditingCard";

    private final List<VideoEditingCard> videoEditingCards = InMemoryDataset.VIDEO_EDITING_CARDS;
    private final ObservationRegistry observationRegistry;

    public InMemoryVideoEditingCardRepository(ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;
    }

    @Override
    public List<VideoEditingCard> findAll() {
        log.info("[Repository:memory] VideoEditingCard.findAll");

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findAll")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> videoEditingCards);
    }

    @Override
    public Optional<VideoEditingCard> findById(Integer id) {
        log.info("[Repository:memory] VideoEditingCard.findById — id: {}", id);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findById")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> videoEditingCards.stream()
                        .filter(card -> card.id().equals(id))
                        .findFirst());
    }

    @Override
    public List<VideoEditingCard> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("[Repository:memory] VideoEditingCard.findByPriceRange — minPrice: {}, maxPrice: {}",
                minPrice, maxPrice);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByPriceRange")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> videoEditingCards.stream()
                        .filter(card -> card.price().compareTo(minPrice) >= 0
                                && card.price().compareTo(maxPrice) <= 0)
                        .toList());
    }

    @Override
    public Optional<VideoEditingCard> findByName(String name) {
        log.info("[Repository:memory] VideoEditingCard.findByName — name: '{}'", name);

        return Observation.createNotStarted(OBSERVATION_NAME, observationRegistry)
                .lowCardinalityKeyValue("entity", ENTITY)
                .lowCardinalityKeyValue("operation", "findByName")
                .lowCardinalityKeyValue("type", "in-memory")
                .observe(() -> videoEditingCards.stream()
                        .filter(c -> c.name().toLowerCase().contains(name.toLowerCase()))
                        .findFirst());
    }
}
