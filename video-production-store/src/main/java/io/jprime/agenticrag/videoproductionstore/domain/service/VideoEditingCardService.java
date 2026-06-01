package io.jprime.agenticrag.videoproductionstore.domain.service;

import io.jprime.agenticrag.videoproductionstore.domain.converter.VideoEditingCardConverter;
import io.jprime.agenticrag.videoproductionstore.domain.model.VideoEditingCard;
import io.jprime.agenticrag.videoproductionstore.persistence.entity.VideoEditingCardEntity;
import io.jprime.agenticrag.videoproductionstore.persistence.repository.VideoEditingCardRepository;
import io.jprime.agenticrag.videoproductionstore.web.dto.VideoEditingCardDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VideoEditingCardService {

    private static final Logger log = LoggerFactory.getLogger(VideoEditingCardService.class);

    private final VideoEditingCardRepository videoEditingCardRepository;

    public VideoEditingCardService(VideoEditingCardRepository videoEditingCardRepository) {
        this.videoEditingCardRepository = videoEditingCardRepository;
    }

    @Transactional(readOnly = true)
    public List<VideoEditingCardDto> findAll() {
        log.info("[VideoEditingCardService] findAll");

        List<VideoEditingCardDto> results = videoEditingCardRepository.findAll().stream()
                .map(VideoEditingCardConverter::toDomain)
                .map(VideoEditingCardConverter::toDto)
                .toList();
        log.info("[VideoEditingCardService] findAll returned {} result(s)", results.size());

        return results;
    }

    @Transactional(readOnly = true)
    public Optional<VideoEditingCardDto> findById(Integer id) {
        log.info("[VideoEditingCardService] findById — id: {}", id);

        Optional<VideoEditingCardDto> result = videoEditingCardRepository.findById(id)
                .map(VideoEditingCardConverter::toDomain)
                .map(VideoEditingCardConverter::toDto);
        log.info("[VideoEditingCardService] findById result: {}", result.isPresent() ? result.get() : "not found");

        return result;
    }

    @Transactional(readOnly = true)
    public Optional<VideoEditingCardDto> findByName(String name) {
        log.info("[VideoEditingCardService] findByName — name: '{}'", name);

        Optional<VideoEditingCardDto> result = videoEditingCardRepository.findByName(name)
                .map(VideoEditingCardConverter::toDomain)
                .map(VideoEditingCardConverter::toDto);
        log.info("[VideoEditingCardService] findByName result: {}", result.isPresent() ? result.get() : "not found");

        return result;
    }

    @Transactional(readOnly = true)
    public List<VideoEditingCardDto> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("[VideoEditingCardService] findByPriceRange — minPrice: {}, maxPrice: {}", minPrice, maxPrice);

        if (minPrice.compareTo(BigDecimal.ZERO) < 0 || maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Video Editing Card price values cannot be negative.");
        }

        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Video Editing Card minPrice cannot be greater than maxPrice.");
        }

        List<VideoEditingCardDto> results = videoEditingCardRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(VideoEditingCardConverter::toDomain)
                .map(VideoEditingCardConverter::toDto)
                .toList();
        log.info("[VideoEditingCardService] findByPriceRange returned {} result(s)", results.size());

        return results;
    }

    @Transactional
    public VideoEditingCardDto create(VideoEditingCardDto videoEditingCardDto) {
        log.info("[VideoEditingCardService] create — name: '{}'", videoEditingCardDto.name());

        VideoEditingCard videoEditingCard = VideoEditingCardConverter.toDomain(videoEditingCardDto);
        VideoEditingCardEntity videoEditingCardEntity = VideoEditingCardConverter.toEntity(videoEditingCard);
        VideoEditingCardEntity savedVideoEditingCardEntity = videoEditingCardRepository.save(videoEditingCardEntity);
        VideoEditingCard savedVideoEditingCard = VideoEditingCardConverter.toDomain(savedVideoEditingCardEntity);

        VideoEditingCardDto result = VideoEditingCardConverter.toDto(savedVideoEditingCard);
        log.info("[VideoEditingCardService] create saved card with id: {}", result.id());

        return result;
    }

    @Transactional
    public Optional<VideoEditingCardDto> update(Integer id, VideoEditingCardDto videoEditingCardDto) {
        log.info("[VideoEditingCardService] update — id: {}", id);

        Optional<VideoEditingCardDto> result = videoEditingCardRepository.findById(id)
                .map(existingVideoEditingCardEntity -> updateVideoEditingCard(existingVideoEditingCardEntity, videoEditingCardDto));
        log.info("[VideoEditingCardService] update result: {}", result.isPresent() ? "updated" : "not found");

        return result;
    }

    private VideoEditingCardDto updateVideoEditingCard(VideoEditingCardEntity existingVideoEditingCardEntity,
                                                       VideoEditingCardDto videoEditingCardDto) {
        existingVideoEditingCardEntity.setName(videoEditingCardDto.name());
        existingVideoEditingCardEntity.setManufacturer(videoEditingCardDto.manufacturer());
        existingVideoEditingCardEntity.setDescription(videoEditingCardDto.description());
        existingVideoEditingCardEntity.setPrice(videoEditingCardDto.price());

        VideoEditingCardEntity savedVideoEditingCardEntity = videoEditingCardRepository.save(existingVideoEditingCardEntity);
        VideoEditingCard savedVideoEditingCard = VideoEditingCardConverter.toDomain(savedVideoEditingCardEntity);
        return VideoEditingCardConverter.toDto(savedVideoEditingCard);
    }

    @Transactional
    public boolean delete(Integer id) {
        log.info("[VideoEditingCardService] delete — id: {}", id);

        if (!videoEditingCardRepository.existsById(id)) {
            log.info("[VideoEditingCardService] delete — not found id: {}", id);
            return false;
        }
        videoEditingCardRepository.deleteById(id);
        log.info("[VideoEditingCardService] delete — deleted id: {}", id);
        return true;
    }
}
