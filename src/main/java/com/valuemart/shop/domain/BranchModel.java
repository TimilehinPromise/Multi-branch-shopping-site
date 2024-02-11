package com.valuemart.shop.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(chain = true)
public class BranchModel  {

    private String name;

    private String location;


}
