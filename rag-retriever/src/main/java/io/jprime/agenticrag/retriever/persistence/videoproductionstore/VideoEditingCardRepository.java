package io.jprime.agenticrag.retriever.persistence.videoproductionstore;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.VideoEditingCard;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VideoEditingCardRepository {

    List<VideoEditingCard> findAll();

    Optional<VideoEditingCard> findById(Integer id);

    List<VideoEditingCard> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    Optional<VideoEditingCard> findByName(String name);
}