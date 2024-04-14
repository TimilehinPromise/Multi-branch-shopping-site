package com.valuemart.shop.domain.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.valuemart.shop.domain.ProductImageModel;
import com.valuemart.shop.domain.models.Seasons;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private String brand;
    private List<ProductImageModel> images;
    @NotNull
    private Long categoryId;
    @NotNull
    @JsonProperty("subcategoryId")
    private Long subCategoryId;
    @NotNull
    @Min(1)
    private BigDecimal price;
    private boolean enabled;
    private boolean availableInBranch1;
    private boolean availableInBranch2;
    private boolean availableInBranch3;
    private String skuId;
    private String season;

}
