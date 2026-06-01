package io.jprime.agenticrag.videoproductionstore;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("Fix video-production-store tests on a next project stage")
@DisplayName("Order REST API Integration Tests")
class OrderControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/orders";

    @Test
    @DisplayName("GET /api/orders - should return all 6 orders")
    void findAll_shouldReturnAllOrders() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$[0].customer.name").value("Ivan Ivanov"))
                .andExpect(jsonPath("$[0].videoEditingCard.name").value("Movie Machine Pro"))
                .andExpect(jsonPath("$[0].orderNote").value("Urgent delivery"));
    }

    @Test
    @DisplayName("GET /api/orders/{id} - should return order 1 with full nested data")
    void findById_shouldReturnOrderWithFullNestedData() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customer.id").value(1))
                .andExpect(jsonPath("$.customer.name").value("Ivan Ivanov"))
                .andExpect(jsonPath("$.customer.email").value("ivan.ivanov@gmail.com"))
                .andExpect(jsonPath("$.videoEditingCard.id").value(1))
                .andExpect(jsonPath("$.videoEditingCard.name").value("Movie Machine Pro"))
                .andExpect(jsonPath("$.videoEditingCard.price").value(350.00))
                .andExpect(jsonPath("$.orderDate").value("2025-01-15"))
                .andExpect(jsonPath("$.orderNote").value("Urgent delivery"));
    }

    @Test
    @DisplayName("GET /api/orders/{id} - should return 404 for non-existing id")
    void findById_shouldReturn404ForNonExistingId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/orders/customer/1 - should return 2 orders for Ivan Ivanov")
    void findByCustomerId_shouldReturn2OrdersForIvanIvanov() throws Exception {
        mockMvc.perform(get(BASE_URL + "/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].customer.name").value("Ivan Ivanov"))
                .andExpect(jsonPath("$[0].videoEditingCard.name").value("Movie Machine Pro"))
                .andExpect(jsonPath("$[0].orderNote").value("Urgent delivery"))
                .andExpect(jsonPath("$[1].videoEditingCard.name").value("Media 100"))
                .andExpect(jsonPath("$[1].orderNote").value("Only credit card payment"));
    }

    @Test
    @DisplayName("GET /api/orders/customer/3 - should return 2 orders for Georgi Georgiev")
    void findByCustomerId_shouldReturn2OrdersForGeorgiGeorgiev() throws Exception {
        mockMvc.perform(get(BASE_URL + "/customer/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].customer.name").value("Georgi Georgiev"))
                .andExpect(jsonPath("$[0].videoEditingCard.name").value("Movie Machine Pro"))
                .andExpect(jsonPath("$[1].videoEditingCard.name").value("MiroMotion DC30"));
    }

    @Test
    @DisplayName("GET /api/orders/customer/999 - should return empty list for non-existing customer")
    void findByCustomerId_shouldReturnEmptyListForNonExistingCustomer() throws Exception {
        mockMvc.perform(get(BASE_URL + "/customer/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
