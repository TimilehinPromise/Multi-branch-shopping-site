package com.valuemart.shop.domain.util;

import com.valuemart.shop.domain.ResponseMessage;

public class ResponseMessageUtil {

    public static ResponseMessage createSuccessResponse(String entityName, String action) {
        String message = String.format("%s %s successfully", entityName, action);
        return ResponseMessage.builder().message(message).build();
    }

    // Other utility methods for different types of responses...
}
