package com.valuemart.shop.domain.models;

import lombok.Data;

@Data
public class PageableRequest {
    private Long pageNumber;
    private Long pageSize;
}
