package io.jprime.agenticrag.videoproductionstore.persistence.repository;

import io.jprime.agenticrag.videoproductionstore.persistence.entity.VideoEditingCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VideoEditingCardRepository extends JpaRepository<VideoEditingCardEntity, Integer> {

    @Query("SELECT v FROM VideoEditingCardEntity v WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Optional<VideoEditingCardEntity> findByName(@Param("name") String name);

    @Query("SELECT v FROM VideoEditingCardEntity v WHERE v.price >= :minPrice AND v.price <= :maxPrice")
    List<VideoEditingCardEntity> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                                  @Param("maxPrice") BigDecimal maxPrice);
}
