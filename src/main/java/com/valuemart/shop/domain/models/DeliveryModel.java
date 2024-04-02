package com.valuemart.shop.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryModel {

    private String areaName;

    private BigDecimal deliveryPrice;
}
