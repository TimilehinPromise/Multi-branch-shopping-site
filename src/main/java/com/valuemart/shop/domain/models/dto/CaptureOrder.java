package com.valuemart.shop.domain.models.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CaptureOrder {
    private BigDecimal totalAmount;
    private Long userId;
}
