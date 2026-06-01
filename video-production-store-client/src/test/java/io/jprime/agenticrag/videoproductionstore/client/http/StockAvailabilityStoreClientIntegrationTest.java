package io.jprime.agenticrag.videoproductionstore.client.http;

import io.jprime.agenticrag.videoproductionstore.client.dto.StockAvailabilityDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Run manually with video-production-store running on localhost:8082")
@DisplayName("StockAvailabilityClient Integration Tests")
class StockAvailabilityStoreClientIntegrationTest extends AbstractClientIntegrationTest {

    @Test
    @DisplayName("findAll() - should return all 4 stock availabilities")
    void findAll_shouldReturnAllStockAvailabilities() {
        List<StockAvailabilityDto> result = STOCK_AVAILABILITY_STORE_CLIENT.findAll();

        assertThat(result).hasSize(4);
        assertThat(result.get(0).videoEditingCard().name()).isEqualTo("Movie Machine Pro");
        assertThat(result.get(0).availability()).isEqualTo(5);
        assertThat(result.get(1).videoEditingCard().name()).isEqualTo("DPS Velocity");
        assertThat(result.get(1).availability()).isEqualTo(23);
        assertThat(result.get(2).videoEditingCard().name()).isEqualTo("Media 100");
        assertThat(result.get(2).availability()).isEqualTo(47);
        assertThat(result.get(3).videoEditingCard().name()).isEqualTo("MiroMotion DC30");
        assertThat(result.get(3).availability()).isEqualTo(12);
    }

    @Test
    @DisplayName("findByVideoCardId(1) - should return availability 5 for Movie Machine Pro")
    void findByVideoCardId_shouldReturnAvailabilityForMovieMachinePro() {
        Optional<StockAvailabilityDto> result = STOCK_AVAILABILITY_STORE_CLIENT.findByVideoCardId(1);

        assertThat(result).isPresent();
        assertThat(result.get().videoEditingCard().id()).isEqualTo(1);
        assertThat(result.get().videoEditingCard().name()).isEqualTo("Movie Machine Pro");
        assertThat(result.get().availability()).isEqualTo(5);
    }

    @Test
    @DisplayName("findByVideoCardId(3) - should return availability 47 for Media 100")
    void findByVideoCardId_shouldReturnAvailabilityForMedia100() {
        Optional<StockAvailabilityDto> result = STOCK_AVAILABILITY_STORE_CLIENT.findByVideoCardId(3);

        assertThat(result).isPresent();
        assertThat(result.get().videoEditingCard().name()).isEqualTo("Media 100");
        assertThat(result.get().availability()).isEqualTo(47);
    }

    @Test
    @DisplayName("findByVideoCardId(999) - should return empty Optional for non-existing video card")
    void findByVideoCardId_shouldReturnEmptyForNonExistingVideoCard() {
        Optional<StockAvailabilityDto> result = STOCK_AVAILABILITY_STORE_CLIENT.findByVideoCardId(999);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByMinQuantity(20) - should return DPS Velocity and Media 100")
    void findByMinQuantity_shouldReturnCardsWithAtLeast20InStock() {
        List<StockAvailabilityDto> result = STOCK_AVAILABILITY_STORE_CLIENT.findByMinQuantity(20);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).videoEditingCard().name()).isEqualTo("DPS Velocity");
        assertThat(result.get(1).videoEditingCard().name()).isEqualTo("Media 100");
    }

    @Test
    @DisplayName("findByMinQuantity(5) - should return all 4 stock availabilities")
    void findByMinQuantity_shouldReturnAllWithAtLeast5InStock() {
        List<StockAvailabilityDto> result = STOCK_AVAILABILITY_STORE_CLIENT.findByMinQuantity(5);

        assertThat(result).hasSize(4);
    }

    @Test
    @DisplayName("findByMinQuantity(100) - should return empty list when no card has such high stock")
    void findByMinQuantity_shouldReturnEmptyListWhenNoCardHasSuchHighStock() {
        List<StockAvailabilityDto> result = STOCK_AVAILABILITY_STORE_CLIENT.findByMinQuantity(100);

        assertThat(result).isEmpty();
    }
}
