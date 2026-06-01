package io.jprime.agenticrag.videoproductionstore.client.http;

import io.jprime.agenticrag.videoproductionstore.client.dto.CustomerDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Run manually with video-production-store running on localhost:8082")
@DisplayName("CustomerClient Integration Tests")
class CustomerStoreClientIntegrationTest extends AbstractClientIntegrationTest {

    @Test
    @DisplayName("findAll() - should return all 4 customers")
    void findAll_shouldReturnAllCustomers() {
        List<CustomerDto> result = CUSTOMER_STORE_CLIENT.findAll();

        assertThat(result).hasSize(4);
        assertThat(result.get(0).name()).isEqualTo("Ivan Ivanov");
        assertThat(result.get(1).name()).isEqualTo("Petar Petrov");
        assertThat(result.get(2).name()).isEqualTo("Georgi Georgiev");
        assertThat(result.get(3).name()).isEqualTo("Nikolay Nikolaev");
    }

    @Test
    @DisplayName("findById(1) - should return Ivan Ivanov")
    void findById_shouldReturnIvanIvanov() {
        Optional<CustomerDto> result = CUSTOMER_STORE_CLIENT.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1);
        assertThat(result.get().name()).isEqualTo("Ivan Ivanov");
        assertThat(result.get().email()).isEqualTo("ivan.ivanov@gmail.com");
        assertThat(result.get().notes()).isEqualTo("VIP client");
    }

    @Test
    @DisplayName("findById(999) - should return empty Optional for non-existing id")
    void findById_shouldReturnEmptyForNonExistingId() {
        Optional<CustomerDto> result = CUSTOMER_STORE_CLIENT.findById(999);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByName('Ivan') - should return Ivan Ivanov")
    void findByName_shouldReturnIvanIvanov() {
        List<CustomerDto> result = CUSTOMER_STORE_CLIENT.findByName("Ivan");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Ivan Ivanov");
    }

    @Test
    @DisplayName("findByName('ivan') - should return Ivan Ivanov (case-insensitive)")
    void findByName_shouldBeCaseInsensitive() {
        List<CustomerDto> result = CUSTOMER_STORE_CLIENT.findByName("ivan");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Ivan Ivanov");
    }

    @Test
    @DisplayName("findByName('nonexistent') - should return empty list")
    void findByName_shouldReturnEmptyListForNonExistingName() {
        List<CustomerDto> result = CUSTOMER_STORE_CLIENT.findByName("nonexistent");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByVideoCardId(1) - should return Ivan Ivanov and Georgi Georgiev")
    void findByVideoCardId_shouldReturnCustomersWhoOrderedMovieMachinePro() {
        List<CustomerDto> result = CUSTOMER_STORE_CLIENT.findByVideoCardId(1);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Ivan Ivanov");
        assertThat(result.get(1).name()).isEqualTo("Georgi Georgiev");
    }

    @Test
    @DisplayName("findByVideoCardId(2) - should return Petar Petrov and Nikolay Nikolaev")
    void findByVideoCardId_shouldReturnCustomersWhoOrderedDpsVelocity() {
        List<CustomerDto> result = CUSTOMER_STORE_CLIENT.findByVideoCardId(2);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Petar Petrov");
        assertThat(result.get(1).name()).isEqualTo("Nikolay Nikolaev");
    }

    @Test
    @DisplayName("findByVideoCardId(999) - should return empty list for non-existing video card")
    void findByVideoCardId_shouldReturnEmptyListForNonExistingVideoCard() {
        List<CustomerDto> result = CUSTOMER_STORE_CLIENT.findByVideoCardId(999);

        assertThat(result).isEmpty();
    }
}
