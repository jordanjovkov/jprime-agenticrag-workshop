package io.jprime.agenticrag.videoproductionstore.domain.service;

import io.jprime.agenticrag.videoproductionstore.domain.converter.OrderConverter;
import io.jprime.agenticrag.videoproductionstore.domain.model.Order;
import io.jprime.agenticrag.videoproductionstore.persistence.entity.CustomerEntity;
import io.jprime.agenticrag.videoproductionstore.persistence.entity.OrderEntity;
import io.jprime.agenticrag.videoproductionstore.persistence.entity.VideoEditingCardEntity;
import io.jprime.agenticrag.videoproductionstore.persistence.repository.CustomerRepository;
import io.jprime.agenticrag.videoproductionstore.persistence.repository.OrderRepository;
import io.jprime.agenticrag.videoproductionstore.persistence.repository.VideoEditingCardRepository;
import io.jprime.agenticrag.videoproductionstore.web.dto.OrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final VideoEditingCardRepository videoEditingCardRepository;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        VideoEditingCardRepository videoEditingCardRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.videoEditingCardRepository = videoEditingCardRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAll() {
        log.info("[OrderService] findAll");

        List<OrderDto> results = orderRepository.findAll().stream()
                .map(OrderConverter::toDomain)
                .map(OrderConverter::toDto)
                .toList();
        log.info("[OrderService] findAll returned {} result(s)", results.size());

        return results;
    }

    @Transactional(readOnly = true)
    public Optional<OrderDto> findById(Integer id) {
        log.info("[OrderService] findById — id: {}", id);

        Optional<OrderDto> result = orderRepository.findById(id)
                .map(OrderConverter::toDomain)
                .map(OrderConverter::toDto);
        log.info("[OrderService] findById result: {}", result.isPresent() ? result.get() : "not found");

        return result;
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findByCustomerId(Integer customerId) {
        log.info("[OrderService] findByCustomerId — customerId: {}", customerId);

        List<OrderDto> results = orderRepository.findByCustomerId(customerId).stream()
                .map(OrderConverter::toDomain)
                .map(OrderConverter::toDto)
                .toList();
        log.info("[OrderService] findByCustomerId returned {} result(s)", results.size());

        return results;
    }

    @Transactional
    public Optional<OrderDto> create(OrderDto orderDto) {
        log.info("[OrderService] create — customerId: {}, videoEditingCardId: {}",
                orderDto.customer().id(), orderDto.videoEditingCard().id());

        Optional<OrderDto> result = customerRepository.findById(orderDto.customer().id())
                .flatMap(customerEntity -> videoEditingCardRepository.findById(orderDto.videoEditingCard().id())
                        .map(videoEditingCardEntity -> createOrder(customerEntity, videoEditingCardEntity, orderDto)));
        log.info("[OrderService] create result: {}", result.isPresent() ? "created" : "not found");

        return result;
    }

    private OrderDto createOrder(CustomerEntity customerEntity,
                                 VideoEditingCardEntity videoEditingCardEntity,
                                 OrderDto orderDto) {
        OrderEntity newOrderEntity = new OrderEntity();
        newOrderEntity.setCustomer(customerEntity);
        newOrderEntity.setVideoEditingCard(videoEditingCardEntity);
        newOrderEntity.setOrderDate(orderDto.orderDate());
        newOrderEntity.setOrderNote(orderDto.orderNote());

        OrderEntity savedOrderEntity = orderRepository.save(newOrderEntity);
        Order savedOrder = OrderConverter.toDomain(savedOrderEntity);
        return OrderConverter.toDto(savedOrder);
    }

    @Transactional
    public Optional<OrderDto> update(Integer id, OrderDto orderDto) {
        log.info("[OrderService] update — id: {}", id);

        Optional<OrderDto> result = orderRepository.findById(id)
                .flatMap(existingOrderEntity -> customerRepository.findById(orderDto.customer().id())
                        .flatMap(customerEntity -> videoEditingCardRepository.findById(orderDto.videoEditingCard().id())
                                .map(videoEditingCardEntity -> updateOrder(existingOrderEntity, customerEntity, videoEditingCardEntity, orderDto))));
        log.info("[OrderService] update result: {}", result.isPresent() ? "updated" : "not found");

        return result;
    }

    private OrderDto updateOrder(OrderEntity existingOrderEntity,
                                 CustomerEntity customerEntity,
                                 VideoEditingCardEntity videoEditingCardEntity,
                                 OrderDto orderDto) {
        existingOrderEntity.setCustomer(customerEntity);
        existingOrderEntity.setVideoEditingCard(videoEditingCardEntity);
        existingOrderEntity.setOrderDate(orderDto.orderDate());
        existingOrderEntity.setOrderNote(orderDto.orderNote());

        OrderEntity savedOrderEntity = orderRepository.save(existingOrderEntity);
        Order savedOrder = OrderConverter.toDomain(savedOrderEntity);
        return OrderConverter.toDto(savedOrder);
    }

    @Transactional
    public boolean delete(Integer id) {
        log.info("[OrderService] delete — id: {}", id);

        if (!orderRepository.existsById(id)) {
            log.info("[OrderService] delete — not found id: {}", id);
            return false;
        }
        orderRepository.deleteById(id);
        log.info("[OrderService] delete — deleted id: {}", id);
        return true;
    }
}
