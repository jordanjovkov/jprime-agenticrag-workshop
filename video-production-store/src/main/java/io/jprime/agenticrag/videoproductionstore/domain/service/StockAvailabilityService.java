package io.jprime.agenticrag.videoproductionstore.domain.service;

import io.jprime.agenticrag.videoproductionstore.domain.converter.StockAvailabilityConverter;
import io.jprime.agenticrag.videoproductionstore.domain.model.StockAvailability;
import io.jprime.agenticrag.videoproductionstore.persistence.entity.StockAvailabilityEntity;
import io.jprime.agenticrag.videoproductionstore.persistence.entity.VideoEditingCardEntity;
import io.jprime.agenticrag.videoproductionstore.persistence.repository.StockAvailabilityRepository;
import io.jprime.agenticrag.videoproductionstore.persistence.repository.VideoEditingCardRepository;
import io.jprime.agenticrag.videoproductionstore.web.dto.StockAvailabilityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StockAvailabilityService {

    private static final Logger log = LoggerFactory.getLogger(StockAvailabilityService.class);

    private final StockAvailabilityRepository stockAvailabilityRepository;
    private final VideoEditingCardRepository videoEditingCardRepository;

    public StockAvailabilityService(StockAvailabilityRepository stockAvailabilityRepository,
                                    VideoEditingCardRepository videoEditingCardRepository) {
        this.stockAvailabilityRepository = stockAvailabilityRepository;
        this.videoEditingCardRepository = videoEditingCardRepository;
    }

    @Transactional(readOnly = true)
    public List<StockAvailabilityDto> findAll() {
        log.info("[StockAvailabilityService] findAll");

        List<StockAvailabilityDto> results = stockAvailabilityRepository.findAll().stream()
                .map(StockAvailabilityConverter::toDomain)
                .map(StockAvailabilityConverter::toDto)
                .toList();
        log.info("[StockAvailabilityService] findAll returned {} result(s)", results.size());

        return results;
    }

    @Transactional(readOnly = true)
    public Optional<StockAvailabilityDto> findById(Integer id) {
        log.info("[StockAvailabilityService] findById — id: {}", id);

        Optional<StockAvailabilityDto> result = stockAvailabilityRepository.findById(id)
                .map(StockAvailabilityConverter::toDomain)
                .map(StockAvailabilityConverter::toDto);
        log.info("[StockAvailabilityService] findById result: {}", result.isPresent() ? result.get() : "not found");

        return result;
    }

    @Transactional(readOnly = true)
    public Optional<StockAvailabilityDto> findByVideoCardId(Integer videoCardId) {
        log.info("[StockAvailabilityService] findByVideoCardId — videoCardId: {}", videoCardId);

        Optional<StockAvailabilityDto> result = stockAvailabilityRepository.findByVideoEditingCardId(videoCardId)
                .map(StockAvailabilityConverter::toDomain)
                .map(StockAvailabilityConverter::toDto);
        log.info("[StockAvailabilityService] findByVideoCardId result: {}",
                result.isPresent() ? result.get() : "not found");

        return result;
    }

    @Transactional(readOnly = true)
    public List<StockAvailabilityDto> findByMinQuantity(int minQuantity) {
        log.info("[StockAvailabilityService] findByMinQuantity — minQuantity: {}", minQuantity);

        if (minQuantity < 0) {
            throw new IllegalArgumentException("Stock availability minQuantity cannot be negative.");
        }

        List<StockAvailabilityDto> results = stockAvailabilityRepository.findByMinQuantity(minQuantity).stream()
                .map(StockAvailabilityConverter::toDomain)
                .map(StockAvailabilityConverter::toDto)
                .toList();
        log.info("[StockAvailabilityService] findByMinQuantity returned {} result(s)", results.size());

        return results;
    }

    @Transactional
    public Optional<StockAvailabilityDto> create(StockAvailabilityDto stockAvailabilityDto) {
        log.info("[StockAvailabilityService] create — videoEditingCardId: {}, availability: {}",
                stockAvailabilityDto.videoEditingCard().id(), stockAvailabilityDto.availability());

        Optional<StockAvailabilityDto> result = videoEditingCardRepository
                .findById(stockAvailabilityDto.videoEditingCard().id())
                .map(videoEditingCardEntity -> createStockAvailability(videoEditingCardEntity, stockAvailabilityDto));
        log.info("[StockAvailabilityService] create result: {}", result.isPresent() ? "created" : "not found");

        return result;
    }

    private StockAvailabilityDto createStockAvailability(VideoEditingCardEntity videoEditingCardEntity,
                                                         StockAvailabilityDto stockAvailabilityDto) {
        StockAvailabilityEntity newStockAvailabilityEntity = new StockAvailabilityEntity();
        newStockAvailabilityEntity.setVideoEditingCard(videoEditingCardEntity);
        newStockAvailabilityEntity.setAvailability(stockAvailabilityDto.availability());

        StockAvailabilityEntity savedStockAvailabilityEntity = stockAvailabilityRepository.save(newStockAvailabilityEntity);
        StockAvailability savedStockAvailability = StockAvailabilityConverter.toDomain(savedStockAvailabilityEntity);
        return StockAvailabilityConverter.toDto(savedStockAvailability);
    }

    @Transactional
    public Optional<StockAvailabilityDto> update(Integer id, StockAvailabilityDto stockAvailabilityDto) {
        log.info("[StockAvailabilityService] update — id: {}", id);

        Optional<StockAvailabilityDto> result = stockAvailabilityRepository.findById(id)
                .flatMap(existingStockAvailabilityEntity -> videoEditingCardRepository
                        .findById(stockAvailabilityDto.videoEditingCard().id())
                        .map(videoEditingCardEntity -> updateStockAvailability(
                                existingStockAvailabilityEntity, videoEditingCardEntity, stockAvailabilityDto)));
        log.info("[StockAvailabilityService] update result: {}", result.isPresent() ? "updated" : "not found");

        return result;
    }

    private StockAvailabilityDto updateStockAvailability(StockAvailabilityEntity existingStockAvailabilityEntity,
                                                         VideoEditingCardEntity videoEditingCardEntity,
                                                         StockAvailabilityDto stockAvailabilityDto) {
        existingStockAvailabilityEntity.setVideoEditingCard(videoEditingCardEntity);
        existingStockAvailabilityEntity.setAvailability(stockAvailabilityDto.availability());

        StockAvailabilityEntity savedStockAvailabilityEntity = stockAvailabilityRepository.save(existingStockAvailabilityEntity);
        StockAvailability savedStockAvailability = StockAvailabilityConverter.toDomain(savedStockAvailabilityEntity);
        return StockAvailabilityConverter.toDto(savedStockAvailability);
    }

    @Transactional
    public boolean delete(Integer id) {
        log.info("[StockAvailabilityService] delete — id: {}", id);

        if (!stockAvailabilityRepository.existsById(id)) {
            log.info("[StockAvailabilityService] delete — not found id: {}", id);
            return false;
        }
        stockAvailabilityRepository.deleteById(id);
        log.info("[StockAvailabilityService] delete — deleted id: {}", id);
        return true;
    }
}
