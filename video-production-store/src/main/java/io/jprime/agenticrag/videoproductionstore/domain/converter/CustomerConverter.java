package io.jprime.agenticrag.videoproductionstore.domain.converter;

import io.jprime.agenticrag.videoproductionstore.domain.model.Customer;
import io.jprime.agenticrag.videoproductionstore.persistence.entity.CustomerEntity;
import io.jprime.agenticrag.videoproductionstore.web.dto.CustomerDto;

public final class CustomerConverter {

    private CustomerConverter() {}

    public static Customer toDomain(CustomerEntity customerEntity) {
        return Customer.enroll(
                customerEntity.getId(),
                customerEntity.getName(),
                customerEntity.getEmail(),
                customerEntity.getPhone(),
                customerEntity.getAddress(),
                customerEntity.getNotes()
        );
    }

    public static Customer toDomain(CustomerDto customerDto) {
        return Customer.enroll(
                null,
                customerDto.name(),
                customerDto.email(),
                customerDto.phone(),
                customerDto.address(),
                customerDto.notes()
        );
    }

    public static CustomerDto toDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getNotes()
        );
    }

    public static CustomerEntity toEntity(Customer customer) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName(customer.getName());
        customerEntity.setEmail(customer.getEmail());
        customerEntity.setPhone(customer.getPhone());
        customerEntity.setAddress(customer.getAddress());
        customerEntity.setNotes(customer.getNotes());
        return customerEntity;
    }
}
