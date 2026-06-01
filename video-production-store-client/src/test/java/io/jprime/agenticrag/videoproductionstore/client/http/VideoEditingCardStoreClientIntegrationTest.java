package io.jprime.agenticrag.videoproductionstore.client.http;

import io.jprime.agenticrag.videoproductionstore.client.dto.VideoEditingCardDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Run manually with video-production-store running on localhost:8082")
@DisplayName("VideoEditingCardClient Integration Tests")
class VideoEditingCardStoreClientIntegrationTest extends AbstractClientIntegrationTest {

    @Test
    @DisplayName("findAll() - should return all 4 video editing cards")
    void findAll_shouldReturnAllVideoEditingCards() {
        List<VideoEditingCardDto> result = VIDEO_EDITING_CARD_STORE_CLIENT.findAll();

        assertThat(result).hasSize(4);
        assertThat(result.get(0).name()).isEqualTo("Movie Machine Pro");
        assertThat(result.get(1).name()).isEqualTo("DPS Velocity");
        assertThat(result.get(2).name()).isEqualTo("Media 100");
        assertThat(result.get(3).name()).isEqualTo("MiroMotion DC30");
    }

    @Test
    @DisplayName("findById(1) - should return Movie Machine Pro")
    void findById_shouldReturnMovieMachinePro() {
        Optional<VideoEditingCardDto> result = VIDEO_EDITING_CARD_STORE_CLIENT.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1);
        assertThat(result.get().name()).isEqualTo("Movie Machine Pro");
        assertThat(result.get().manufacturer()).isEqualTo("Fast Multimedia AG");
        assertThat(result.get().price()).isEqualByComparingTo(new BigDecimal("350.00"));
    }

    @Test
    @DisplayName("findById(999) - should return empty Optional for non-existing id")
    void findById_shouldReturnEmptyForNonExistingId() {
        Optional<VideoEditingCardDto> result = VIDEO_EDITING_CARD_STORE_CLIENT.findById(999);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByName('Media') - should return Media 100")
    void findByName_shouldReturnMedia100() {
        Optional<VideoEditingCardDto> result = VIDEO_EDITING_CARD_STORE_CLIENT.findByName("Media");

        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("Media 100");
        assertThat(result.get().manufacturer()).isEqualTo("Data Translation");
    }

    @Test
    @DisplayName("findByName('media') - should return Media 100 (case-insensitive)")
    void findByName_shouldBeCaseInsensitive() {
        Optional<VideoEditingCardDto> result = VIDEO_EDITING_CARD_STORE_CLIENT.findByName("media");

        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("Media 100");
    }

    @Test
    @DisplayName("findByName('NVidia') - should return empty Optional for non-existing name")
    void findByName_shouldReturnEmptyForNonExistingName() {
        Optional<VideoEditingCardDto> result = VIDEO_EDITING_CARD_STORE_CLIENT.findByName("NVidia");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByPriceRange(200, 400) - should return Movie Machine Pro and MiroMotion DC30")
    void findByPriceRange_shouldReturnTwoCards() {
        List<VideoEditingCardDto> result = VIDEO_EDITING_CARD_STORE_CLIENT.findByPriceRange(new BigDecimal("200.00"), new BigDecimal("400.00"));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Movie Machine Pro");
        assertThat(result.get(1).name()).isEqualTo("MiroMotion DC30");
    }

    @Test
    @DisplayName("findByPriceRange(1000, 2000) - should return DPS Velocity")
    void findByPriceRange_shouldReturnDpsVelocity() {
        List<VideoEditingCardDto> result = VIDEO_EDITING_CARD_STORE_CLIENT.findByPriceRange(new BigDecimal("1000.00"), new BigDecimal("20000.00"));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("DPS Velocity");
    }

    @Test
    @DisplayName("findByPriceRange(5000, 9000) - should return empty list for out-of-range prices")
    void findByPriceRange_shouldReturnEmptyListForOutOfRangePrices() {
        List<VideoEditingCardDto> result = VIDEO_EDITING_CARD_STORE_CLIENT.findByPriceRange(new BigDecimal("5000.00"), new BigDecimal("90000.00"));

        assertThat(result).isEmpty();
    }
}
