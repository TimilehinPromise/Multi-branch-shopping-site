package com.valuemart.shop.domain.util;

import com.valuemart.shop.persistence.entity.Product;

public final class ProductUtil {


    public static String generateSkuId(Product product) {
        // Extract the first two characters of the product name
        String productNamePrefix = product.getName().length() >= 2
                ? product.getName().substring(0, 2).toUpperCase()
                : product.getName().toUpperCase();


        String categoryId = String.valueOf(product.getBusinessCategory().getId());
        String subcategoryId = String.valueOf(product.getBusinessSubcategory().getId());
        String productId = String.valueOf(product.getId());

        // Combine the attributes with some delimiters
        return "SKU" + productNamePrefix + "_" + categoryId + subcategoryId  + productId;
    }

    public static String generateTempSkuId(Product product) {
        // Extract the first two characters of the product name
        String productNamePrefix = product.getName().length() >= 2
                ? product.getName().substring(0, 2).toUpperCase()
                : product.getName().toUpperCase();


        String categoryId = String.valueOf(product.getBusinessCategory().getId());
        String subcategoryId = String.valueOf(product.getBusinessSubcategory().getId());
        String randomNo = String.valueOf(Math.random());
        System.out.println(randomNo);


        // Combine the attributes with some delimiters
        return "SKU-" + productNamePrefix + "-" + randomNo + "-" + subcategoryId + "-" + categoryId;
    }



}


