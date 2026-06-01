package io.jprime.agenticrag.videoproductionstore;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("Fix video-production-store tests on a next project stage")
@DisplayName("StockAvailability REST API Integration Tests")
class StockAvailabilityControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/stock-availabilities";

    @Test
    @DisplayName("GET /api/stock-availabilities - should return all 4 stock availabilities")
    void findAll_shouldReturnAllStockAvailabilities() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].videoEditingCard.name").value("Movie Machine Pro"))
                .andExpect(jsonPath("$[0].availability").value(5))
                .andExpect(jsonPath("$[1].videoEditingCard.name").value("DPS Velocity"))
                .andExpect(jsonPath("$[1].availability").value(23))
                .andExpect(jsonPath("$[2].videoEditingCard.name").value("Media 100"))
                .andExpect(jsonPath("$[2].availability").value(47))
                .andExpect(jsonPath("$[3].videoEditingCard.name").value("MiroMotion DC30"))
                .andExpect(jsonPath("$[3].availability").value(12));
    }

    @Test
    @DisplayName("GET /api/stock-availabilities/{id} - should return stock for Movie Machine Pro")
    void findById_shouldReturnStockForMovieMachinePro() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.videoEditingCard.name").value("Movie Machine Pro"))
                .andExpect(jsonPath("$.availability").value(5));
    }

    @Test
    @DisplayName("GET /api/stock-availabilities/{id} - should return 404 for non-existing id")
    void findById_shouldReturn404ForNonExistingId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/stock-availabilities/video-card/1 - should return availability 5 for Movie Machine Pro")
    void findByVideoCardId_shouldReturnAvailabilityForMovieMachinePro() throws Exception {
        mockMvc.perform(get(BASE_URL + "/video-card/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.videoEditingCard.id").value(1))
                .andExpect(jsonPath("$.videoEditingCard.name").value("Movie Machine Pro"))
                .andExpect(jsonPath("$.availability").value(5));
    }

    @Test
    @DisplayName("GET /api/stock-availabilities/video-card/3 - should return availability 47 for Media 100")
    void findByVideoCardId_shouldReturnAvailabilityForMedia100() throws Exception {
        mockMvc.perform(get(BASE_URL + "/video-card/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.videoEditingCard.name").value("Media 100"))
                .andExpect(jsonPath("$.availability").value(47));
    }

    @Test
    @DisplayName("GET /api/stock-availabilities/min-quantity?minQuantity=20 - should return DPS Velocity and Media 100")
    void findByMinQuantity_shouldReturnCardsWithAtLeast20InStock() throws Exception {
        mockMvc.perform(get(BASE_URL + "/min-quantity").param("minQuantity", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].videoEditingCard.name").value("DPS Velocity"))
                .andExpect(jsonPath("$[1].videoEditingCard.name").value("Media 100"));
    }

    @Test
    @DisplayName("GET /api/stock-availabilities/min-quantity?minQuantity=5 - should return all 4")
    void findByMinQuantity_shouldReturnAllWithAtLeast5InStock() throws Exception {
        mockMvc.perform(get(BASE_URL + "/min-quantity").param("minQuantity", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }
}
