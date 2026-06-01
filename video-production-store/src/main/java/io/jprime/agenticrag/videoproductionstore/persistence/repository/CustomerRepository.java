package io.jprime.agenticrag.videoproductionstore.persistence.repository;

import io.jprime.agenticrag.videoproductionstore.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

    @Query("SELECT c FROM CustomerEntity c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<CustomerEntity> findByName(@Param("name") String name);

    @Query("""
            SELECT DISTINCT c FROM CustomerEntity c
            JOIN OrderEntity o ON o.customer.id = c.id
            WHERE o.videoEditingCard.id = :videoCardId
            """)
    List<CustomerEntity> findByVideoCardId(@Param("videoCardId") Integer videoCardId);
}
