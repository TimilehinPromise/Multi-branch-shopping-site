package com.valuemart.shop.persistence.specification;

import lombok.Data;

@Data
public class SearchCriteria<T> {
    private String key;
    private T value;
    private SearchOperation operation;

    public SearchCriteria() {
    }

    public SearchCriteria(String key, T value, SearchOperation operation) {
        this.key = key;
        this.value = value;
        this.operation = operation;
    }
}
