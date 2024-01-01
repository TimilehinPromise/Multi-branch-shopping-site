package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Optional<Orders>findFirstByIdAndBranchId(Long id, Long branchId);
}
