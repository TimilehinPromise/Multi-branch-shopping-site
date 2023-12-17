package com.valuemart.shop.domain.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public enum RelatedBy {

    BRAND("brand"),

    SUBCATEGORY("subcategory");



    @Getter
    private final String relatedByName;
}
