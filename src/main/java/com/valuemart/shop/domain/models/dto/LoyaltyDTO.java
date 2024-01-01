package com.valuemart.shop.domain.models.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoyaltyDTO {
    private Long count;

    private BigDecimal discountValue;

}
