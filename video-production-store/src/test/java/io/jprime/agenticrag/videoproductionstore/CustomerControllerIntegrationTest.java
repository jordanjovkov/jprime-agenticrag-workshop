package io.jprime.agenticrag.videoproductionstore;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("Fix video-production-store tests on a next project stage")
@DisplayName("Customer REST API Integration Tests")
class CustomerControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/customers";

    @Test
    @DisplayName("GET /api/customers - should return all 4 customers")
    void findAll_shouldReturnAllCustomers() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].name").value("Ivan Ivanov"))
                .andExpect(jsonPath("$[1].name").value("Petar Petrov"))
                .andExpect(jsonPath("$[2].name").value("Georgi Georgiev"))
                .andExpect(jsonPath("$[3].name").value("Nikolay Nikolaev"));
    }

    @Test
    @DisplayName("GET /api/customers/{id} - should return Ivan Ivanov for id=1")
    void findById_shouldReturnIvanIvanov() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan Ivanov"))
                .andExpect(jsonPath("$.email").value("ivan.ivanov@gmail.com"))
                .andExpect(jsonPath("$.notes").value("VIP client"));
    }

    @Test
    @DisplayName("GET /api/customers/{id} - should return 404 for non-existing id")
    void findById_shouldReturn404ForNonExistingId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/customers/name?name=Ivan - should return Ivan Ivanov")
    void findByName_shouldReturnIvanIvanov() throws Exception {
        mockMvc.perform(get(BASE_URL + "/name").param("name", "Ivan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Ivan Ivanov"));
    }

    @Test
    @DisplayName("GET /api/customers/name?name=ivan - should return Ivan Ivanov (case-insensitive)")
    void findByName_shouldBeCaseInsensitive() throws Exception {
        mockMvc.perform(get(BASE_URL + "/name").param("name", "ivan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Ivan Ivanov"));
    }

    @Test
    @DisplayName("GET /api/customers/name?name=nonexistent - should return empty list")
    void findByName_shouldReturnEmptyListForNonExistingName() throws Exception {
        mockMvc.perform(get(BASE_URL + "/name").param("name", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/customers/video-card/1 - should return Ivan Ivanov and Georgi Georgiev")
    void findByVideoCardId_shouldReturnCustomersWhoOrderedMovieMachinePro() throws Exception {
        mockMvc.perform(get(BASE_URL + "/video-card/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Ivan Ivanov"))
                .andExpect(jsonPath("$[1].name").value("Georgi Georgiev"));
    }

    @Test
    @DisplayName("GET /api/customers/video-card/2 - should return Petar Petrov and Nikolay Nikolaev")
    void findByVideoCardId_shouldReturnCustomersWhoOrderedDpsVelocity() throws Exception {
        mockMvc.perform(get(BASE_URL + "/video-card/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Petar Petrov"))
                .andExpect(jsonPath("$[1].name").value("Nikolay Nikolaev"));
    }
}
