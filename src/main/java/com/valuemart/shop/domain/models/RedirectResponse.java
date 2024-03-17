package com.valuemart.shop.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RedirectResponse {

    private String message;

    private boolean success;
}
