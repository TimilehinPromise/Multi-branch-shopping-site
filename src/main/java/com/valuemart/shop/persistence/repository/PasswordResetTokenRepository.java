package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {

    Optional<PasswordResetToken> findByResetToken(String token);
}
