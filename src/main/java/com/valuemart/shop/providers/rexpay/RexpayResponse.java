package com.valuemart.shop.providers.rexpay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RexpayResponse {

    private String reference;
    private String clientId;

    @JsonProperty("paymentUrl")
    private String paymentURL;

    private String status;
    private String paymentChannel;

    @JsonProperty("paymentUrlReference")
    private String paymentURLReference;

    @JsonProperty("externalPaymentReference")
    private String externalPaymentReference;

    private BigDecimal fees;
    private String currency;
}
