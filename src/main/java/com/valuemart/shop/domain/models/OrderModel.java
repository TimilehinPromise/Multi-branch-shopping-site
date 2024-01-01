package com.valuemart.shop.domain.models;

import com.valuemart.shop.domain.models.enums.OrderStatus;

import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Builder
@Data
public class OrderModel {
    private BigDecimal amount;
    private BigDecimal discountedAmount;
    private String address;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private UserModel user;
    private String paymentProvider;
    private CartModel details;
    private String message;
    private String product;
    private Long branchId;
}
