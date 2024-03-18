package com.valuemart.shop.controller;

import com.valuemart.shop.domain.models.ProductModel;
import com.valuemart.shop.domain.service.abstracts.ProductsService;
import com.valuemart.shop.domain.util.UserUtils;
import com.valuemart.shop.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping(path = "v1/api/customer/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductCustomerController {
    private final ProductsService productsService;


    @GetMapping("/getAll")
    public List<ProductModel> getAllProduct() {
        User user = UserUtils.getLoggedInUser();
        return productsService.getAllProductStore(Long.valueOf(user.getBranchId()));
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductModel> getAllProductByCategory(@PathVariable Long categoryId) {
        return productsService.getAllProductByCategory(categoryId);
    }

    @GetMapping("/subcategory/{subcategoryId}")
    public List<ProductModel> getAllProductBySubCategory(@PathVariable Long subcategoryId) {
        return productsService.getAllProductBySubCategory(subcategoryId);
    }

    @GetMapping("/{sku}")
    public ProductModel getAllProductBySku(@PathVariable String sku) {
        return productsService.getProductBySkuId(sku);
    }

    @GetMapping("/relatedBy/{sku}")
    public List<ProductModel> getAllProductRelatedBy(@RequestParam String related, @RequestParam String keyword,@PathVariable String sku ) {
        return productsService.getProductRelatedBy(related,keyword,sku);
    }

    @GetMapping("/season")
    public List<ProductModel> getAllProductBySeason() {
        User user = UserUtils.getLoggedInUser();
        return productsService.getProductsBySeasonStore(Long.valueOf(user.getBranchId()));
    }


    @GetMapping("/search")
    public Page<ProductModel> search(@RequestParam Long branchId, @RequestParam String keyword,@PageableDefault(sort = "id", direction = DESC) Pageable pageable) {
        return productsService.searchProducts(keyword,branchId,pageable);
    }

}
