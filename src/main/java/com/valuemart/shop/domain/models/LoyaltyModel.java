package com.valuemart.shop.domain.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class LoyaltyModel {

    private BigDecimal requiredAmount;

    private int coinNo;

    private BigDecimal discountValue;
}
