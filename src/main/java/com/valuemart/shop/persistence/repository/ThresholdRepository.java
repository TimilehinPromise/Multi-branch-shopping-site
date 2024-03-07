package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.Threshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ThresholdRepository extends JpaRepository<Threshold, Long> {

    Optional<Threshold> findByValue(BigDecimal amount);

    @Query("SELECT t FROM Threshold t WHERE t.value <= :value ORDER BY t.value DESC")
    List<Threshold> findTopByValueLessThanEqualOrderByValueDesc(@Param("value") BigDecimal value);

}
