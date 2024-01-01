package com.valuemart.shop.domain.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class LoyaltyModel {

    private Long count;

    private BigDecimal discountValue;
}
