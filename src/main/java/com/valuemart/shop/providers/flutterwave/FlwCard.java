package com.valuemart.shop.providers.flutterwave;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"firstSixDigits", "lastFourDigits"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlwCard {

    @JsonProperty("first_6digits")
    private String firstSixDigits;

    @JsonProperty("last_4digits")
    private String lastFourDigits;

    @JsonProperty("issuer")
    private String issuer;

    @JsonProperty("country")
    private String country;

    @JsonProperty("type")
    private String type;

    @JsonProperty("expiry")
    private String expiry;
}
