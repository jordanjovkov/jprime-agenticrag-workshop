package io.jprime.agenticrag.retriever.domain.llm.tool;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Customer;
import io.jprime.agenticrag.retriever.domain.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerQueryTools {

    private static final Logger log = LoggerFactory.getLogger(CustomerQueryTools.class);

    private final CustomerService customerService;

    public CustomerQueryTools(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Tool(description = """
            Returns a list of customers matching the specified name.
            Supports partial name matching.
            Use this tool when the user asks to find or identify a customer by name.
            """)
    public List<Customer> findCustomersByName(String name) {
        log.info("[Tool:local] findCustomersByName called — name: '{}'", name);

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name parameter cannot be blank.");
        }

        List<Customer> results = customerService.findCustomersByName(name);
        log.info("[Tool:local] findCustomersByName returned {} result(s)", results.size());

        return results;
    }

    @Tool(description = """
            Returns a single customer by their unique ID.
            Use this tool when the user asks for details about a specific customer
            and the customer ID is already known.
            """)
    public Object getCustomerById(Integer id) {
        log.info("[Tool:local] getCustomerById called — id: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Customer ID must be a positive number.");
        }

        Object result = customerService.getCustomerById(id)
                .map(customer -> (Object) customer)
                .orElse("No customer found with ID " + id + ".");
        log.info("[Tool:local] getCustomerById result: {}", result);

        return result;
    }

    @Tool(description = """
            Returns a list of customers who have purchased the specified video editing card.
            Use this tool when the user asks which customers bought a particular card,
            or wants to find customers interested in a specific product.
            """)
    public List<Customer> getCustomersByVideoCardId(Integer videoCardId) {
        log.info("[Tool:local] getCustomersByVideoCardId called — videoCardId: {}", videoCardId);

        if (videoCardId == null || videoCardId <= 0) {
            throw new IllegalArgumentException("Video card ID must be a positive number.");
        }

        List<Customer> results = customerService.getCustomersByVideoCardId(videoCardId);
        log.info("[Tool:local] getCustomersByVideoCardId returned {} result(s)", results.size());

        return results;
    }
}
