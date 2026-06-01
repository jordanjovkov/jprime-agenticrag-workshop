package io.jprime.agenticrag.retriever.domain.llm.tool;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.StockAvailability;
import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.VideoEditingCard;
import io.jprime.agenticrag.retriever.domain.service.VideoEditingCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class VideoEditingCardQueryTools {

    private static final Logger log = LoggerFactory.getLogger(VideoEditingCardQueryTools.class);

    private final VideoEditingCardService videoEditingCardService;

    public VideoEditingCardQueryTools(VideoEditingCardService videoEditingCardService) {
        this.videoEditingCardService = videoEditingCardService;
    }

    @Tool(description = """
            Returns a list of video editing cards within the specified price range.
            Use this tool when the user asks about video cards filtered by price,
            budget constraints, or wants to find affordable or premium options.
            """)
    public List<VideoEditingCard> getVideoCardsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("[Tool:local] getVideoCardsByPriceRange called — minPrice: {}, maxPrice: {}", minPrice, maxPrice);

        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Video Editing Card price values cannot be null.");
        }

        if (minPrice.compareTo(BigDecimal.ZERO) < 0 || maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Video Editing Card price values cannot be negative.");
        }

        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Video Editing Card minPrice cannot be greater than maxPrice.");
        }

        List<VideoEditingCard> results = videoEditingCardService.getVideoCardsByPriceRange(minPrice, maxPrice);
        log.info("[Tool:local] getVideoCardsByPriceRange returned {} result(s)", results.size());

        return results;
    }

    @Tool(description = """
            Returns a list of video editing cards with stock availability
            equal to or above the specified minimum quantity.
            Use this tool when the user asks about available stock,
            inventory levels, or wants to find cards that are in stock.
            """)
    public List<StockAvailability> getVideoCardsByStockAvailability(int minQuantity) {
        log.info("[Tool:local] getVideoCardsByStockAvailability called — minQuantity: {}", minQuantity);

        if (minQuantity < 0) {
            throw new IllegalArgumentException("minQuantity cannot be negative.");
        }

        List<StockAvailability> results = videoEditingCardService.getVideoCardsByStockAvailability(minQuantity);
        log.info("[Tool:local] getVideoCardsByStockAvailability returned {} result(s)", results.size());

        return results;
    }

    @Tool(description = """
        Returns a video editing card by its name.
        Use this tool when you need to find the ID or full details of a specific
        video editing card by name, before calling other tools that require a card ID.
        """)
    public Object getVideoCardByName(String name) {
        log.info("[Tool:local] getVideoCardByName called — name: '{}'", name);

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Video Editing Card name parameter cannot be blank.");
        }

        Object result = videoEditingCardService.findByName(name)
                .map(card -> (Object) card)
                .orElse("No video editing card found with name '" + name + "'.");
        log.info("[Tool:local] getVideoCardByName result: {}", result);

        return result;
    }
}
