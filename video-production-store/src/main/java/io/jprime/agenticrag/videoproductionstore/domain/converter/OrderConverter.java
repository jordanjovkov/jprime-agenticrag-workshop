package io.jprime.agenticrag.videoproductionstore.domain.converter;

import io.jprime.agenticrag.videoproductionstore.domain.model.Order;
import io.jprime.agenticrag.videoproductionstore.persistence.entity.OrderEntity;
import io.jprime.agenticrag.videoproductionstore.web.dto.OrderDto;

public final class OrderConverter {

    private OrderConverter() {}

    public static Order toDomain(OrderEntity orderEntity) {
        return Order.place(
                orderEntity.getId(),
                CustomerConverter.toDomain(orderEntity.getCustomer()),
                VideoEditingCardConverter.toDomain(orderEntity.getVideoEditingCard()),
                orderEntity.getOrderDate(),
                orderEntity.getOrderNote()
        );
    }

    public static OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId(),
                CustomerConverter.toDto(order.getCustomer()),
                VideoEditingCardConverter.toDto(order.getVideoEditingCard()),
                order.getOrderDate(),
                order.getOrderNote()
        );
    }
}
