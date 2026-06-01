package io.jprime.agenticrag.retriever.domain.llm.tool;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Order;
import io.jprime.agenticrag.retriever.domain.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderQueryTools {

    private static final Logger log = LoggerFactory.getLogger(OrderQueryTools.class);

    private final OrderService orderService;

    public OrderQueryTools(OrderService orderService) {
        this.orderService = orderService;
    }

    @Tool(description = """
            Returns a list of all orders placed by the specified customer.
            Each order includes full customer details and the purchased video editing card.
            Use this tool when the user asks about a customer's purchase history
            or wants to see what a specific customer has ordered.
            """)
    public List<Order> getOrdersByCustomerId(Integer customerId) {
        log.info("[Tool:local] getOrdersByCustomerId called — customerId: {}", customerId);

        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be a positive number.");
        }

        List<Order> results = orderService.getOrdersByCustomerId(customerId);
        log.info("[Tool:local] getOrdersByCustomerId returned {} result(s)", results.size());

        return results;
    }
}
