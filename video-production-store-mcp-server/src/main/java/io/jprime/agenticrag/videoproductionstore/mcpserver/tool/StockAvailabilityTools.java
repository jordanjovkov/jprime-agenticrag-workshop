package io.jprime.agenticrag.videoproductionstore.mcpserver.tool;

import io.jprime.agenticrag.videoproductionstore.client.dto.StockAvailabilityDto;
import io.jprime.agenticrag.videoproductionstore.client.http.StockAvailabilityStoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockAvailabilityTools {

    private static final Logger log = LoggerFactory.getLogger(StockAvailabilityTools.class);

    private final StockAvailabilityStoreClient stockAvailabilityStoreClient;

    public StockAvailabilityTools(StockAvailabilityStoreClient stockAvailabilityStoreClient) {
        this.stockAvailabilityStoreClient = stockAvailabilityStoreClient;
    }

    @Tool(description = """
            Returns all stock availability records in the store.
            Use when the user asks for a full inventory overview.
            """)
    public List<StockAvailabilityDto> getAllStockAvailabilities() {
        log.info("[Tool:mcp-server] getAllStockAvailabilities called");

        List<StockAvailabilityDto> results = stockAvailabilityStoreClient.findAll();
        log.info("[Tool:mcp-server] getAllStockAvailabilities returned {} result(s)", results.size());

        return results;
    }

    @Tool(description = """
            Returns the stock availability record for a specific video editing card.
            Use when the user asks how many units of a particular product are in stock.
            Returns a message if no stock record exists for that card.
            """)
    public Object getStockAvailabilityByVideoCardId(
            @ToolParam(description = "ID of the video editing card") Integer videoCardId) {
        log.info("[Tool:mcp-server] getStockAvailabilityByVideoCardId called — videoCardId: {}", videoCardId);

        if (videoCardId == null || videoCardId <= 0) {
            throw new IllegalArgumentException("Video card ID must be a positive number.");
        }

        Optional<StockAvailabilityDto> stock = stockAvailabilityStoreClient.findByVideoCardId(videoCardId);
        Object result = stock.isPresent() ? stock.get()
                : "No stock availability record found for video editing card with ID " + videoCardId + ".";
        log.info("[Tool:mcp-server] getStockAvailabilityByVideoCardId result: {}", result);

        return result;
    }

    @Tool(description = """
            Returns a list of video editing cards with stock availability
            equal to or above the specified minimum quantity.
            Use this tool when the user asks about available stock,
            inventory levels, or wants to find cards that are in stock.
            """)
    public List<StockAvailabilityDto> getStockAvailabilitiesByMinQuantity(
            @ToolParam(description = "Minimum stock quantity threshold (inclusive)") int minQuantity) {
        log.info("[Tool:mcp-server] getStockAvailabilitiesByMinQuantity called — minQuantity: {}", minQuantity);

        if (minQuantity < 0) {
            throw new IllegalArgumentException("minQuantity cannot be negative.");
        }

        List<StockAvailabilityDto> results = stockAvailabilityStoreClient.findByMinQuantity(minQuantity);
        log.info("[Tool:mcp-server] getStockAvailabilitiesByMinQuantity returned {} result(s)", results.size());

        return results;
    }
}
