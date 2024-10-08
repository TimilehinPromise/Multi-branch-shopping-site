package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.Cart;
import com.valuemart.shop.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUserOrderByCreatedAtDesc(User user);

    boolean existsByIdAndUser(Long cartId, User user);

    List<Cart> deleteByUser(User user);
}
