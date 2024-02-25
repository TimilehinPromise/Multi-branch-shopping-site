package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.DeliveryArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryAreaRepository extends JpaRepository<DeliveryArea, Integer> {
    Optional<DeliveryArea> findByAreaName(String areaName);
}
