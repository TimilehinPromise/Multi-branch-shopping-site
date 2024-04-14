package com.valuemart.shop.domain.models;

import com.valuemart.shop.domain.models.enums.OrderStatus;

import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class OrderModel {
    private Long orderId;
    private BigDecimal amount;
    private BigDecimal discountedAmount;
    private String address;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private UserModel user;
    private String paymentProvider;
    private CartModel details;
    private String message;
    private Long branchId;
    private BigDecimal deliveryAmount;
    private String shopResponse;
    private String orderCode;
    private LocalDateTime createdAt;
}
