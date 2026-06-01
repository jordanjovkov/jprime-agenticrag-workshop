package io.jprime.agenticrag.videoproductionstore.persistence.repository;

import io.jprime.agenticrag.videoproductionstore.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    List<OrderEntity> findByCustomerId(Integer customerId);
}
