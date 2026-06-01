package io.jprime.agenticrag.videoproductionstore.mcpserver.tool;

import io.jprime.agenticrag.videoproductionstore.client.dto.OrderDto;
import io.jprime.agenticrag.videoproductionstore.client.http.OrderStoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderTools {

    private static final Logger log = LoggerFactory.getLogger(OrderTools.class);

    private final OrderStoreClient orderStoreClient;

    public OrderTools(OrderStoreClient orderStoreClient) {
        this.orderStoreClient = orderStoreClient;
    }

    @Tool(description = """
            Returns all orders placed in the store.
            Use when the user asks to list or review all orders.
            """)
    public List<OrderDto> getAllOrders() {
        log.info("[Tool:mcp-server] getAllOrders called");

        List<OrderDto> results = orderStoreClient.findAll();
        log.info("[Tool:mcp-server] getAllOrders returned {} result(s)", results.size());

        return results;
    }

    @Tool(description = """
            Returns a single order by its numeric ID.
            Use when the user references a specific order by ID.
            Returns a message if no order is found with that ID.
            """)
    public Object getOrderById(
            @ToolParam(description = "The numeric ID of the order") Integer id) {
        log.info("[Tool:mcp-server] getOrderById called — id: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Order ID must be a positive number.");
        }

        Optional<OrderDto> order = orderStoreClient.findById(id);
        Object result = order.isPresent() ? order.get() : "No order found with ID " + id + ".";
        log.info("[Tool:mcp-server] getOrderById result: {}", result);

        return result;
    }

    @Tool(description = """
            Returns a list of all orders placed by the specified customer.
            Each order includes full customer details and the purchased video editing card.
            Use this tool when the user asks about a customer's purchase history
            or wants to see what a specific customer has ordered.
            """)
    public List<OrderDto> getOrdersByCustomerId(
            @ToolParam(description = "ID of the customer whose orders to retrieve") Integer customerId) {
        log.info("[Tool:mcp-server] getOrdersByCustomerId called — customerId: {}", customerId);

        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be a positive number.");
        }

        List<OrderDto> results = orderStoreClient.findByCustomerId(customerId);
        log.info("[Tool:mcp-server] getOrdersByCustomerId returned {} result(s)", results.size());

        return results;
    }
}
