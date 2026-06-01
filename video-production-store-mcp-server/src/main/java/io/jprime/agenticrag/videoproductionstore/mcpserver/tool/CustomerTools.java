package io.jprime.agenticrag.videoproductionstore.mcpserver.tool;

import io.jprime.agenticrag.videoproductionstore.client.dto.CustomerDto;
import io.jprime.agenticrag.videoproductionstore.client.http.CustomerStoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerTools {

    private static final Logger log = LoggerFactory.getLogger(CustomerTools.class);

    private final CustomerStoreClient customerStoreClient;

    public CustomerTools(CustomerStoreClient customerStoreClient) {
        this.customerStoreClient = customerStoreClient;
    }

    @Tool(description = """
            Returns all customers registered in the store.
            Use when the user asks to list or browse customers.
            """)
    public List<CustomerDto> getAllCustomers() {
        log.info("[Tool:mcp-server] getAllCustomers called");

        List<CustomerDto> results = customerStoreClient.findAll();
        log.info("[Tool:mcp-server] getAllCustomers returned {} result(s)", results.size());

        return results;
    }

    @Tool(description = """
            Returns a single customer by their unique ID.
            Use this tool when the user asks for details about a specific customer
            and the customer ID is already known.
            """)
    public Object getCustomerById(
            @ToolParam(description = "The numeric ID of the customer") Integer id) {
        log.info("[Tool:mcp-server] getCustomerById called — id: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Customer ID must be a positive number.");
        }

        Optional<CustomerDto> customer = customerStoreClient.findById(id);
        Object result = customer.isPresent() ? customer.get() : "No customer found with ID " + id + ".";
        log.info("[Tool:mcp-server] getCustomerById result: {}", result);

        return result;
    }

    @Tool(description = """
            Returns a list of customers matching the specified name.
            Supports partial name matching.
            Use this tool when the user asks to find or identify a customer by name.
            """)
    public List<CustomerDto> findCustomersByName(
            @ToolParam(description = "Partial or full customer name to search for") String name) {
        log.info("[Tool:mcp-server] findCustomersByName called — name: '{}'", name);

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name parameter cannot be blank.");
        }

        List<CustomerDto> results = customerStoreClient.findByName(name);
        log.info("[Tool:mcp-server] findCustomersByName returned {} result(s)", results.size());

        return results;
    }

    @Tool(description = """
            Returns a list of customers who have purchased the specified video editing card.
            Use this tool when the user asks which customers bought a particular card,
            or wants to find customers interested in a specific product.
            """)
    public List<CustomerDto> getCustomersByVideoCardId(
            @ToolParam(description = "ID of the video editing card") Integer videoCardId) {
        log.info("[Tool:mcp-server] getCustomersByVideoCardId called — videoCardId: {}", videoCardId);

        if (videoCardId == null || videoCardId <= 0) {
            throw new IllegalArgumentException("Video card ID must be a positive number.");
        }

        List<CustomerDto> results = customerStoreClient.findByVideoCardId(videoCardId);
        log.info("[Tool:mcp-server] getCustomersByVideoCardId returned {} result(s)", results.size());

        return results;
    }
}
