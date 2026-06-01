package io.jprime.agenticrag.retriever.persistence.videoproductionstore;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    List<Customer> findAll();

    Optional<Customer> findById(Integer id);

    List<Customer> findByName(String name);

    List<Customer> findByVideoCardId(Integer videoCardId);
}