package io.jprime.agenticrag.videoproductionstore.domain.converter;

import io.jprime.agenticrag.videoproductionstore.domain.model.StockAvailability;
import io.jprime.agenticrag.videoproductionstore.persistence.entity.StockAvailabilityEntity;
import io.jprime.agenticrag.videoproductionstore.web.dto.StockAvailabilityDto;

public final class StockAvailabilityConverter {

    private StockAvailabilityConverter() {}

    public static StockAvailability toDomain(StockAvailabilityEntity stockAvailabilityEntity) {
        return StockAvailability.initialize(
                stockAvailabilityEntity.getId(),
                VideoEditingCardConverter.toDomain(stockAvailabilityEntity.getVideoEditingCard()),
                stockAvailabilityEntity.getAvailability()
        );
    }

    public static StockAvailabilityDto toDto(StockAvailability stockAvailability) {
        return new StockAvailabilityDto(
                stockAvailability.getId(),
                VideoEditingCardConverter.toDto(stockAvailability.getVideoEditingCard()),
                stockAvailability.getAvailability()
        );
    }
}
