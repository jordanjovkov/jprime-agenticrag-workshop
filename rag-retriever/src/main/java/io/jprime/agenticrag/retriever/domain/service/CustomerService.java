package io.jprime.agenticrag.retriever.domain.service;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Customer;
import io.jprime.agenticrag.retriever.persistence.videoproductionstore.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findCustomersByName(String name) {
        log.info("[Service] findCustomersByName — name: '{}'", name);

        List<Customer> results = customerRepository.findByName(name);
        log.info("[Service] findCustomersByName returned {} result(s)", results.size());

        return results;
    }

    public Optional<Customer> getCustomerById(Integer id) {
        log.info("[Service] getCustomerById — id: {}", id);

        Optional<Customer> result = customerRepository.findById(id);
        log.info("[Service] getCustomerById result: {}", result.isPresent() ? result.get() : "not found");

        return result;
    }

    public List<Customer> getCustomersByVideoCardId(Integer videoCardId) {
        log.info("[Service] getCustomersByVideoCardId — videoCardId: {}", videoCardId);

        List<Customer> results = customerRepository.findByVideoCardId(videoCardId);
        log.info("[Service] getCustomersByVideoCardId returned {} result(s)", results.size());

        return results;
    }
}
