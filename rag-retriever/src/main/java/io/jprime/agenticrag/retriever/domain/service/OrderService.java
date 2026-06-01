package io.jprime.agenticrag.retriever.domain.service;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Order;
import io.jprime.agenticrag.retriever.persistence.videoproductionstore.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrdersByCustomerId(Integer customerId) {
        log.info("[Service] getOrdersByCustomerId — customerId: {}", customerId);

        List<Order> results = orderRepository.findByCustomerId(customerId);
        log.info("[Service] getOrdersByCustomerId returned {} result(s)", results.size());

        return results;
    }
}
