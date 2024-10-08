package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.domain.models.enums.OrderStatus;
import com.valuemart.shop.persistence.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Optional<Orders>findFirstByIdAndBranchId(Long id, Long branchId);

    Optional<Orders>findFirstByOrderCodeAndBranchId(String code, Long branchId);

    List<Orders>findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Orders> findFirstByUserIdAndStatusAndBranchIdOrderByCreatedAtDesc(Long userId, OrderStatus status,Long branchId);

    Optional<Orders> findFirstByUserIdAndStatusInAndBranchIdOrderByCreatedAtDesc(Long userId, Collection<OrderStatus> statuses, Long branchId);

    List<Orders> findAllByStatusAndBranchIdOrderByCreatedAtDesc(OrderStatus status, Long branchId);
}
