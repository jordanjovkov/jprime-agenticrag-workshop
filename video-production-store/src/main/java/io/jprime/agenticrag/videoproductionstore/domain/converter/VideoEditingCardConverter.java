package io.jprime.agenticrag.videoproductionstore.domain.converter;

import io.jprime.agenticrag.videoproductionstore.domain.model.VideoEditingCard;
import io.jprime.agenticrag.videoproductionstore.persistence.entity.VideoEditingCardEntity;
import io.jprime.agenticrag.videoproductionstore.web.dto.VideoEditingCardDto;

public final class VideoEditingCardConverter {

    private VideoEditingCardConverter() {}

    public static VideoEditingCard toDomain(VideoEditingCardEntity videoEditingCardEntity) {
        return VideoEditingCard.register(
                videoEditingCardEntity.getId(),
                videoEditingCardEntity.getName(),
                videoEditingCardEntity.getManufacturer(),
                videoEditingCardEntity.getDescription(),
                videoEditingCardEntity.getPrice()
        );
    }

    public static VideoEditingCard toDomain(VideoEditingCardDto videoEditingCardDto) {
        return VideoEditingCard.register(
                null,
                videoEditingCardDto.name(),
                videoEditingCardDto.manufacturer(),
                videoEditingCardDto.description(),
                videoEditingCardDto.price()
        );
    }

    public static VideoEditingCardDto toDto(VideoEditingCard videoEditingCard) {
        return new VideoEditingCardDto(
                videoEditingCard.getId(),
                videoEditingCard.getName(),
                videoEditingCard.getManufacturer(),
                videoEditingCard.getDescription(),
                videoEditingCard.getPrice()
        );
    }

    public static VideoEditingCardEntity toEntity(VideoEditingCard videoEditingCard) {
        VideoEditingCardEntity videoEditingCardEntity = new VideoEditingCardEntity();
        videoEditingCardEntity.setName(videoEditingCard.getName());
        videoEditingCardEntity.setManufacturer(videoEditingCard.getManufacturer());
        videoEditingCardEntity.setDescription(videoEditingCard.getDescription());
        videoEditingCardEntity.setPrice(videoEditingCard.getPrice());
        return videoEditingCardEntity;
    }
}
