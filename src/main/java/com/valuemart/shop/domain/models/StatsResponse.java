package com.valuemart.shop.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsResponse {

    private Long staff;

    private Long payment;

    private Long product;
}
