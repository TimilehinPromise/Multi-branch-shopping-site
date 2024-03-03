package com.valuemart.shop.controller;


import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.ProductModel;
import com.valuemart.shop.domain.models.dto.ProductDTO;
import com.valuemart.shop.domain.service.abstracts.ProductsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping(path = "v1/api/admin/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductAdminController {

    private final ProductsService productsService;

    @PostMapping("")
    public ResponseMessage createProduct(@Valid @RequestBody ProductDTO request) {
        log.info(request.toString());
        return productsService.createProduct(request);
    }

    @PutMapping("")
    public ResponseMessage updateProduct(@Valid @RequestBody ProductDTO request) {
        log.info(request.toString());
        return productsService.updateProduct(request);
    }

    @PostMapping("/bulk")
    public List<String>  createProductWithCsv(@RequestParam("file")MultipartFile file) {
        return productsService.processExcelFileToProducts(file);
    }

    @GetMapping("/category/{categoryId}")
    public Page<ProductModel> getAllProductByCategory(@PageableDefault(sort = "id", direction = DESC) Pageable pageable, @PathVariable Long categoryId) {
        return productsService.getAllProductByCategory(categoryId,pageable);
    }

    @GetMapping("")
    public Page<ProductModel> getAllProduct(@PageableDefault(sort = "id", direction = DESC) Pageable pageable) {
        return productsService.getAllProduct(pageable);
    }

    @GetMapping("/subcategory/{subcategoryId}")
    public Page<ProductModel> getAllProductBySubCategory(@PageableDefault(sort = "id", direction = DESC) Pageable pageable,@PathVariable Long subcategoryId) {
        return productsService.getAllProductBySubCategory(subcategoryId,pageable);
    }

    @GetMapping("/{sku}")
    public ProductModel getAllProductBySku(@PathVariable String sku) {
        return productsService.getProductBySkuId(sku);
    }


    @GetMapping("/relatedBy/{sku}")
    public List<ProductModel> getAllProductRelatedBy(@RequestParam String related, @RequestParam String keyword, @PathVariable String sku ) {
        return productsService.getProductRelatedBy(related,keyword,sku);
    }


    @GetMapping("/search")
    public Page<ProductModel> search(@RequestParam Long branchId, @RequestParam String keyword,@PageableDefault(sort = "id", direction = DESC) Pageable pageable) {
        return productsService.searchProducts(keyword,branchId,pageable);
    }

}
