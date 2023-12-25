package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.Cart;
import com.valuemart.shop.persistence.entity.Loyalty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyRepository extends JpaRepository<Loyalty, Long> {
}
