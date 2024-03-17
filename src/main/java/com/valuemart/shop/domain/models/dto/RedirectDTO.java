package com.valuemart.shop.domain.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RedirectDTO {

    @JsonProperty("tx_ref")
    private String transRef;

    private String status;

    @JsonProperty("transaction_id")
    private String transId;
}
