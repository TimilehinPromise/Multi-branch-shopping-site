package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
