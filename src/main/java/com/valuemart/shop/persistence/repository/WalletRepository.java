package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query(value = "SELECT * FROM wallet WHERE user_id = ?1 LIMIT 1", nativeQuery = true)
    Optional<Wallet> findFirstByUserId(Long userId);
}
