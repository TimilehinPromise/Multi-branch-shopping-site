package com.valuemart.shop.providers.rexpay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RexpayDTO {
    private String reference;
    private BigDecimal amount;
    private String currency;
    private String userId;
    private String callbackUrl;
    private Map<String,Object> metadata;
}
