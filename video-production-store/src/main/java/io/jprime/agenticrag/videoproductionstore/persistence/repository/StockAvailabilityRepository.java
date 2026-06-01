package io.jprime.agenticrag.videoproductionstore.persistence.repository;

import io.jprime.agenticrag.videoproductionstore.persistence.entity.StockAvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockAvailabilityRepository extends JpaRepository<StockAvailabilityEntity, Integer> {

    Optional<StockAvailabilityEntity> findByVideoEditingCardId(Integer videoEditingCardId);

    @Query("SELECT s FROM StockAvailabilityEntity s WHERE s.availability >= :minQuantity")
    List<StockAvailabilityEntity> findByMinQuantity(@Param("minQuantity") int minQuantity);
}
