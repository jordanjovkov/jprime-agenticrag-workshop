package io.jprime.agenticrag.retriever.domain.service;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.StockAvailability;
import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.VideoEditingCard;
import io.jprime.agenticrag.retriever.persistence.videoproductionstore.StockAvailabilityRepository;
import io.jprime.agenticrag.retriever.persistence.videoproductionstore.VideoEditingCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VideoEditingCardService {

    private static final Logger log = LoggerFactory.getLogger(VideoEditingCardService.class);

    private final VideoEditingCardRepository videoEditingCardRepository;
    private final StockAvailabilityRepository stockAvailabilityRepository;

    public VideoEditingCardService(VideoEditingCardRepository videoEditingCardRepository,
                                   StockAvailabilityRepository stockAvailabilityRepository) {
        this.videoEditingCardRepository = videoEditingCardRepository;
        this.stockAvailabilityRepository = stockAvailabilityRepository;
    }

    public List<VideoEditingCard> getVideoCardsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("[Service] getVideoCardsByPriceRange — minPrice: {}, maxPrice: {}", minPrice, maxPrice);

        List<VideoEditingCard> results = videoEditingCardRepository.findByPriceRange(minPrice, maxPrice);
        log.info("[Service] getVideoCardsByPriceRange returned {} result(s)", results.size());

        return results;
    }

    public List<StockAvailability> getVideoCardsByStockAvailability(int minQuantity) {
        log.info("[Service] getVideoCardsByStockAvailability — minQuantity: {}", minQuantity);

        List<StockAvailability> results = stockAvailabilityRepository.findByMinQuantity(minQuantity);
        log.info("[Service] getVideoCardsByStockAvailability returned {} result(s)", results.size());

        return results;
    }

    public Optional<VideoEditingCard> findByName(String name) {
        log.info("[Service] findByName — name: '{}'", name);

        Optional<VideoEditingCard> result = videoEditingCardRepository.findByName(name);
        log.info("[Service] findByName result: {}", result.isPresent() ? result.get() : "not found");

        return result;
    }
}
