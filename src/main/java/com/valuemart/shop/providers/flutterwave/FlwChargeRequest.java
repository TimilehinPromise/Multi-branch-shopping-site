package com.valuemart.shop.providers.flutterwave;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@ToString
public class FlwChargeRequest {
    @JsonProperty("tx_ref")
    private String transactionRef;

    private BigDecimal amount;

    private String currency;

    @JsonProperty("redirect_url")
    private String redirectUrl;

    private FlwCustomer customer;


}
