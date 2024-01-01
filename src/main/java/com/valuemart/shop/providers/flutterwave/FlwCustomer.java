package com.valuemart.shop.providers.flutterwave;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlwCustomer {

    private String email;

    private String name;

}
