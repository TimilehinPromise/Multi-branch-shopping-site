package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Payment.PaymentReference> {
}
