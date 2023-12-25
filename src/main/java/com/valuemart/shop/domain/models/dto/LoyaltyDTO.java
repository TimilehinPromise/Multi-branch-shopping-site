package com.valuemart.shop.domain.models.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoyaltyDTO {

    private BigDecimal requiredAmount;

    private int coinNo;

    private BigDecimal discountValue;

}
