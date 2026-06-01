package io.jprime.agenticrag.videoproductionstore.mcpserver.tool;

import io.jprime.agenticrag.videoproductionstore.client.dto.VideoEditingCardDto;
import io.jprime.agenticrag.videoproductionstore.client.http.VideoEditingCardStoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VideoEditingCardTools {

    private static final Logger log = LoggerFactory.getLogger(VideoEditingCardTools.class);

    private final VideoEditingCardStoreClient videoEditingCardStoreClient;

    public VideoEditingCardTools(VideoEditingCardStoreClient videoEditingCardStoreClient) {
        this.videoEditingCardStoreClient = videoEditingCardStoreClient;
    }

    @Tool(description = """
            Returns all video editing cards in the store catalog.
            Use when the user asks to list or browse available products.
            """)
    public List<VideoEditingCardDto> getAllVideoEditingCards() {
        log.info("[Tool:mcp-server] getAllVideoEditingCards called");

        List<VideoEditingCardDto> results = videoEditingCardStoreClient.findAll();
        log.info("[Tool:mcp-server] getAllVideoEditingCards returned {} result(s)", results.size());

        return results;
    }

    @Tool(description = """
            Returns a single video editing card by its numeric ID.
            Use when the user references a specific product by ID.
            Returns a message if no card is found with that ID.
            """)
    public Object getVideoEditingCardById(
            @ToolParam(description = "The numeric ID of the video editing card") Integer id) {
        log.info("[Tool:mcp-server] getVideoEditingCardById called — id: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Video editing card ID must be a positive number.");
        }

        Optional<VideoEditingCardDto> card = videoEditingCardStoreClient.findById(id);
        Object result = card.isPresent() ? card.get() : "No video editing card found with ID " + id + ".";
        log.info("[Tool:mcp-server] getVideoEditingCardById result: {}", result);

        return result;
    }

    @Tool(description = """
            Returns a video editing card by its name.
            Use this tool when you need to find the ID or full details of a specific
            video editing card by name, before calling other tools that require a card ID.
            """)
    public Object getVideoEditingCardByName(
            @ToolParam(description = "Exact or partial product name to search for") String name) {
        log.info("[Tool:mcp-server] getVideoEditingCardByName called — name: '{}'", name);

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name parameter cannot be blank.");
        }

        Optional<VideoEditingCardDto> card = videoEditingCardStoreClient.findByName(name);
        Object result = card.isPresent() ? card.get() : "No video editing card found with name '" + name + "'.";
        log.info("[Tool:mcp-server] getVideoEditingCardByName result: {}", result);

        return result;
    }

    @Tool(description = """
            Returns a list of video editing cards within the specified price range.
            Use this tool when the user asks about video cards filtered by price,
            budget constraints, or wants to find affordable or premium options.
            """)
    public List<VideoEditingCardDto> getVideoEditingCardsByPriceRange(
            @ToolParam(description = "Minimum price (inclusive)") BigDecimal minPrice,
            @ToolParam(description = "Maximum price (inclusive)") BigDecimal maxPrice) {
        log.info("[Tool:mcp-server] getVideoEditingCardsByPriceRange called — minPrice: {}, maxPrice: {}",
                minPrice, maxPrice);

        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Price values cannot be null.");
        }

        if (minPrice.compareTo(BigDecimal.ZERO) < 0 || maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price values cannot be negative.");
        }

        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("minPrice cannot be greater than maxPrice.");
        }

        List<VideoEditingCardDto> results = videoEditingCardStoreClient.findByPriceRange(minPrice, maxPrice);
        log.info("[Tool:mcp-server] getVideoEditingCardsByPriceRange returned {} result(s)", results.size());

        return results;
    }
}
