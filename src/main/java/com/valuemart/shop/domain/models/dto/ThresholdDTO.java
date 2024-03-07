package com.valuemart.shop.domain.models.dto;

import lombok.Data;
import lombok.ToString;
import java.math.BigDecimal;

@Data
@ToString
public class ThresholdDTO {

    private Long id;

    private BigDecimal value;

    private BigDecimal monetaryAmount;
}
