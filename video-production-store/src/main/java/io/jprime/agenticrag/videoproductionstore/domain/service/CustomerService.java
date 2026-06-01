package io.jprime.agenticrag.videoproductionstore.domain.service;

import io.jprime.agenticrag.videoproductionstore.domain.converter.CustomerConverter;
import io.jprime.agenticrag.videoproductionstore.domain.model.Customer;
import io.jprime.agenticrag.videoproductionstore.persistence.entity.CustomerEntity;
import io.jprime.agenticrag.videoproductionstore.persistence.repository.CustomerRepository;
import io.jprime.agenticrag.videoproductionstore.web.dto.CustomerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> findAll() {
        log.info("[CustomerService] findAll");

        List<CustomerDto> results = customerRepository.findAll().stream()
                .map(CustomerConverter::toDomain)
                .map(CustomerConverter::toDto)
                .toList();
        log.info("[CustomerService] findAll returned {} result(s)", results.size());

        return results;
    }

    @Transactional(readOnly = true)
    public Optional<CustomerDto> findById(Integer id) {
        log.info("[CustomerService] findById — id: {}", id);

        Optional<CustomerDto> result = customerRepository.findById(id)
                .map(CustomerConverter::toDomain)
                .map(CustomerConverter::toDto);
        log.info("[CustomerService] findById result: {}", result.isPresent() ? result.get() : "not found");

        return result;
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> findByName(String name) {
        log.info("[CustomerService] findByName — name: '{}'", name);

        List<CustomerDto> results = customerRepository.findByName(name).stream()
                .map(CustomerConverter::toDomain)
                .map(CustomerConverter::toDto)
                .toList();
        log.info("[CustomerService] findByName returned {} result(s)", results.size());

        return results;
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> findByVideoCardId(Integer videoCardId) {
        log.info("[CustomerService] findByVideoCardId — videoCardId: {}", videoCardId);

        List<CustomerDto> results = customerRepository.findByVideoCardId(videoCardId).stream()
                .map(CustomerConverter::toDomain)
                .map(CustomerConverter::toDto)
                .toList();
        log.info("[CustomerService] findByVideoCardId returned {} result(s)", results.size());

        return results;
    }

    @Transactional
    public CustomerDto create(CustomerDto customerDto) {
        log.info("[CustomerService] create — name: '{}'", customerDto.name());

        Customer customer = CustomerConverter.toDomain(customerDto);
        CustomerEntity customerEntity = CustomerConverter.toEntity(customer);
        CustomerEntity savedCustomerEntity = customerRepository.save(customerEntity);
        Customer savedCustomer = CustomerConverter.toDomain(savedCustomerEntity);

        CustomerDto result = CustomerConverter.toDto(savedCustomer);
        log.info("[CustomerService] create saved customer with id: {}", result.id());

        return result;
    }

    @Transactional
    public Optional<CustomerDto> update(Integer id, CustomerDto customerDto) {
        log.info("[CustomerService] update — id: {}", id);

        Optional<CustomerDto> result = customerRepository.findById(id)
                .map(existingCustomerEntity -> updateCustomer(existingCustomerEntity, customerDto));
        log.info("[CustomerService] update result: {}", result.isPresent() ? "updated" : "not found");

        return result;
    }

    private CustomerDto updateCustomer(CustomerEntity existingCustomerEntity, CustomerDto customerDto) {
        existingCustomerEntity.setName(customerDto.name());
        existingCustomerEntity.setEmail(customerDto.email());
        existingCustomerEntity.setPhone(customerDto.phone());
        existingCustomerEntity.setAddress(customerDto.address());
        existingCustomerEntity.setNotes(customerDto.notes());

        CustomerEntity savedCustomerEntity = customerRepository.save(existingCustomerEntity);
        Customer savedCustomer = CustomerConverter.toDomain(savedCustomerEntity);
        return CustomerConverter.toDto(savedCustomer);
    }

    @Transactional
    public boolean delete(Integer id) {
        log.info("[CustomerService] delete — id: {}", id);

        if (!customerRepository.existsById(id)) {
            log.info("[CustomerService] delete — not found id: {}", id);
            return false;
        }
        customerRepository.deleteById(id);
        log.info("[CustomerService] delete — deleted id: {}", id);
        return true;
    }
}
