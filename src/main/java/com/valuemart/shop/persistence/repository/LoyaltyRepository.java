package com.valuemart.shop.persistence.repository;


import com.valuemart.shop.persistence.entity.Loyalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface LoyaltyRepository extends JpaRepository<Loyalty, Long> {
//    @Query(value = "SELECT * FROM loyalty WHERE count <= :count ORDER BY count DESC LIMIT 1", nativeQuery = true)
//    Optional<Loyalty> findDiscountByCount(@Param("count") Long count);


    Optional<Loyalty>findFirstByCount(Long count);

}
