package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.domain.models.enums.OrderStatus;
import com.valuemart.shop.persistence.entity.Payment;
import com.valuemart.shop.persistence.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Payment.PaymentReference> {

    Optional<Payment> findByPaymentReferenceReferenceIdAndStatus(String reference, PaymentStatus status);

    long countAllByStatus(PaymentStatus status);
}
