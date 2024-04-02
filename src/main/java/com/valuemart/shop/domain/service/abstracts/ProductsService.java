package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.ProductModel;
import com.valuemart.shop.domain.models.dto.ProductDTO;
import com.valuemart.shop.persistence.entity.BusinessCategory;
import com.valuemart.shop.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductsService {
    ResponseMessage createProduct(ProductDTO dto);

    ResponseMessage updateProduct(ProductDTO dto);

    List<ProductModel> getAllProduct();

    long getAllProducts();

    List<ProductModel> getAllProductStore(Long branchId);

    List<ProductModel>  getAllProductByCategory(Long id);

    List<ProductModel>  getAllProductBySubCategory(Long id);

    ProductModel getProductBySkuId(String skuId);

    List<ProductModel> getRecentlyViewed(User user);

    List<BusinessCategory> getCategories();

    List<ProductModel> getProductRelatedBy(String related, String keyword, String sku);

    List<ProductModel> getProductRelatedBy(String brand, String productSku,Long branchId);

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
