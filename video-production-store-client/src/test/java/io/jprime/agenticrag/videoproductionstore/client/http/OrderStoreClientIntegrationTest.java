package io.jprime.agenticrag.videoproductionstore.client.http;

import io.jprime.agenticrag.videoproductionstore.client.dto.OrderDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Run manually with video-production-store running on localhost:8082")
@DisplayName("OrderClient Integration Tests")
class OrderStoreClientIntegrationTest extends AbstractClientIntegrationTest {

    @Test
    @DisplayName("findAll() - should return all 6 orders")
    void findAll_shouldReturnAllOrders() {
        List<OrderDto> result = ORDER_STORE_CLIENT.findAll();

        assertThat(result).hasSize(6);
        assertThat(result.get(0).customer().name()).isEqualTo("Ivan Ivanov");
        assertThat(result.get(0).videoEditingCard().name()).isEqualTo("Movie Machine Pro");
        assertThat(result.get(0).orderNote()).isEqualTo("Urgent delivery");
    }

    @Test
    @DisplayName("findById(1) - should return order with full nested data")
    void findById_shouldReturnOrderWithFullNestedData() {
        Optional<OrderDto> result = ORDER_STORE_CLIENT.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1);
        assertThat(result.get().customer().id()).isEqualTo(1);
        assertThat(result.get().customer().name()).isEqualTo("Ivan Ivanov");
        assertThat(result.get().customer().email()).isEqualTo("ivan.ivanov@gmail.com");
        assertThat(result.get().videoEditingCard().id()).isEqualTo(1);
        assertThat(result.get().videoEditingCard().name()).isEqualTo("Movie Machine Pro");
        assertThat(result.get().videoEditingCard().price()).isEqualByComparingTo(new BigDecimal("350.00"));
        assertThat(result.get().orderDate()).isEqualTo(LocalDate.of(2025, 1, 15));
        assertThat(result.get().orderNote()).isEqualTo("Urgent delivery");
    }

    @Test
    @DisplayName("findById(999) - should return empty Optional for non-existing id")
    void findById_shouldReturnEmptyForNonExistingId() {
        Optional<OrderDto> result = ORDER_STORE_CLIENT.findById(999);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByCustomerId(1) - should return 2 orders for Ivan Ivanov")
    void findByCustomerId_shouldReturn2OrdersForIvanIvanov() {
        List<OrderDto> result = ORDER_STORE_CLIENT.findByCustomerId(1);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).customer().name()).isEqualTo("Ivan Ivanov");
        assertThat(result.get(0).videoEditingCard().name()).isEqualTo("Movie Machine Pro");
        assertThat(result.get(0).orderNote()).isEqualTo("Urgent delivery");
        assertThat(result.get(1).videoEditingCard().name()).isEqualTo("Media 100");
        assertThat(result.get(1).orderNote()).isEqualTo("Only credit card payment");
    }

    @Test
    @DisplayName("findByCustomerId(3) - should return 2 orders for Georgi Georgiev")
    void findByCustomerId_shouldReturn2OrdersForGeorgiGeorgiev() {
        List<OrderDto> result = ORDER_STORE_CLIENT.findByCustomerId(3);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).customer().name()).isEqualTo("Georgi Georgiev");
        assertThat(result.get(0).videoEditingCard().name()).isEqualTo("Movie Machine Pro");
        assertThat(result.get(1).videoEditingCard().name()).isEqualTo("MiroMotion DC30");
    }

    @Test
    @DisplayName("findByCustomerId(999) - should return empty list for non-existing customer")
    void findByCustomerId_shouldReturnEmptyListForNonExistingCustomer() {
        List<OrderDto> result = ORDER_STORE_CLIENT.findByCustomerId(999);

        assertThat(result).isEmpty();
    }
}
