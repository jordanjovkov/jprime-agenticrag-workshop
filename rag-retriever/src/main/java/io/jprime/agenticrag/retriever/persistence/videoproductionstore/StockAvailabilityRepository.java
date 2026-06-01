package io.jprime.agenticrag.retriever.persistence.videoproductionstore;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.StockAvailability;

import java.util.List;
import java.util.Optional;

public interface StockAvailabilityRepository {

    List<StockAvailability> findAll();

    Optional<StockAvailability> findByVideoCardId(Integer videoCardId);

    List<StockAvailability> findByMinQuantity(int minQuantity);
}