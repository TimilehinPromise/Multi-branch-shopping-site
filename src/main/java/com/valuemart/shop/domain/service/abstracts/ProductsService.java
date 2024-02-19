package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.ProductModel;
import com.valuemart.shop.domain.models.dto.ProductDTO;
import com.valuemart.shop.persistence.entity.BusinessCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductsService {
    ResponseMessage createProduct(ProductDTO dto);

    ResponseMessage updateProduct(ProductDTO dto);

    Page<ProductModel> getAllProduct(Pageable pageable);

    Page<ProductModel> getAllProductStore(Long branchId, Pageable pageable);

    Page<ProductModel>  getAllProductByCategory(Long id, Pageable pageable);

    Page<ProductModel>  getAllProductBySubCategory(Long id,Pageable pageable);

    ProductModel getProductBySkuId(String skuId);

    List<BusinessCategory> getCategories();

    List<ProductModel> getProductRelatedBy(String related, String keyword, String sku);

    List<ProductModel> getProductsBySeason();

    List<ProductModel> getProductsBySeasonStore(Long branchId);

    Page<ProductModel> filterProducts(String skuId,
                                      Long productName,
                                      String branch,
                                      Pageable pageable);

    Page<ProductModel> searchProducts(String keyword,
                                      Long branchId,
                                      Pageable pageable);

    List<String> processExcelFileToProducts(MultipartFile file);

}
