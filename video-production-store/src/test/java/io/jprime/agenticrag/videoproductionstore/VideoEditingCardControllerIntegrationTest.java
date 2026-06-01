package io.jprime.agenticrag.videoproductionstore;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("Fix video-production-store tests on a next project stage")
@DisplayName("VideoEditingCard REST API Integration Tests")
class VideoEditingCardControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/video-editing-cards";

    @Test
    @DisplayName("GET /api/video-editing-cards - should return all 4 video editing cards")
    void findAll_shouldReturnAllVideoEditingCards() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].name").value("Movie Machine Pro"))
                .andExpect(jsonPath("$[1].name").value("DPS Velocity"))
                .andExpect(jsonPath("$[2].name").value("Media 100"))
                .andExpect(jsonPath("$[3].name").value("MiroMotion DC30"));
    }

    @Test
    @DisplayName("GET /api/video-editing-cards/{id} - should return Movie Machine Pro for id=1")
    void findById_shouldReturnMovieMachinePro() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Movie Machine Pro"))
                .andExpect(jsonPath("$.manufacturer").value("Fast Multimedia AG"))
                .andExpect(jsonPath("$.price").value(350.00));
    }

    @Test
    @DisplayName("GET /api/video-editing-cards/{id} - should return 404 for non-existing id")
    void findById_shouldReturn404ForNonExistingId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/video-editing-cards/name?name=Media - should return Media 100")
    void findByName_shouldReturnMedia100() throws Exception {
        mockMvc.perform(get(BASE_URL + "/name").param("name", "Media"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Media 100"))
                .andExpect(jsonPath("$.manufacturer").value("Data Translation"));
    }

    @Test
    @DisplayName("GET /api/video-editing-cards/name?name=media - should return Media 100 (case-insensitive)")
    void findByName_shouldBeCaseInsensitive() throws Exception {
        mockMvc.perform(get(BASE_URL + "/name").param("name", "media"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Media 100"));
    }

    @Test
    @DisplayName("GET /api/video-editing-cards/price?minPrice=200&maxPrice=400 - should return Movie Machine Pro and MiroMotion DC30")
    void findByPriceRange_shouldReturnMovieMachineProAndMiroMotionDC30() throws Exception {
        mockMvc.perform(get(BASE_URL + "/price")
                        .param("minPrice", "200")
                        .param("maxPrice", "400"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Movie Machine Pro"))
                .andExpect(jsonPath("$[1].name").value("MiroMotion DC30"));
    }

    @Test
    @DisplayName("GET /api/video-editing-cards/price?minPrice=1000&maxPrice=2000 - should return DPS Velocity")
    void findByPriceRange_shouldReturnDpsVelocity() throws Exception {
        mockMvc.perform(get(BASE_URL + "/price")
                        .param("minPrice", "1000")
                        .param("maxPrice", "2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("DPS Velocity"));
    }
}
