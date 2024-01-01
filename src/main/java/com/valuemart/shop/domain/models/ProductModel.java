package com.valuemart.shop.domain.models;

import com.valuemart.shop.domain.ProductImageModel;
import com.valuemart.shop.persistence.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductModel {


    @NotBlank
    private String name;
    private String skuId;
    @NotBlank
    private String description;
    private String brand;
    private List<ProductImageModel> images;
    @NotBlank
    private String categoryName;
    @NotBlank
    private String subcategoryName;
    @NotNull
    @Min(1)
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean enabled;


}
