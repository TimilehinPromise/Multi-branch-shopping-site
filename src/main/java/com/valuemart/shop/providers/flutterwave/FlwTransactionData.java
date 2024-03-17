package com.valuemart.shop.providers.flutterwave;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlwTransactionData {

    @JsonProperty("id")
    private int id;

    @JsonProperty("tx_ref")
    private String txRef;

    @JsonProperty("flw_ref")
    private String flwRef;

    @JsonProperty("device_fingerprint")
    private String deviceFingerprint;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("charged_amount")
    private BigDecimal chargedAmount;

    @JsonProperty("app_fee")
    private BigDecimal appFee;

    @JsonProperty("merchant_fee")
    private BigDecimal merchantFee;

    @JsonProperty("processor_response")
    private String processorResponse;

    @JsonProperty("auth_model")
    private String authModel;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("narration")
    private String narration;

    @JsonProperty("status")
    private String status;

    @JsonProperty("auth_url")
    private String authUrl;

    @JsonProperty("payment_type")
    private String paymentType;

    @JsonProperty("fraud_status")
    private String fraudStatus;

    @JsonProperty("charge_type")
    private String chargeType;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("account_id")
    private int accountId;

    @JsonProperty("customer")
    private FlwCustomer customer;

    @JsonProperty("card")
    private FlwCard card;


}
