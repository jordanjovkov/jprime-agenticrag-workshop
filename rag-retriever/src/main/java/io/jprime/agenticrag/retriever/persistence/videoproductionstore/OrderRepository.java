package io.jprime.agenticrag.retriever.persistence.videoproductionstore;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    List<Order> findAll();

    Optional<Order> findById(Integer id);

    List<Order> findByCustomerId(Integer customerId);
}