package com.valuemart.shop.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class GetAddressAmount {

    private BigDecimal amount;
}
