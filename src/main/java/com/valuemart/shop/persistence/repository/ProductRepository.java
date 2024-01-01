package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.BusinessCategory;
import com.valuemart.shop.persistence.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {


    boolean existsProductByNameIgnoreCaseAndDeletedFalse(String name);

    Page<Product> findAllByBusinessCategoryIdAndDeletedFalse(Long categoryId, Pageable pageable);

    Page<Product>  findAllByBusinessSubcategoryIdAndDeletedFalse(Long subCategoryId, Pageable pageable);


    Optional<Product>findFirstBySkuIdAndDeletedFalse(String skuId);

    Optional<Product> findFirstBySkuIdAndDeletedFalseAndBranches_Id(String skuId, Long branchId);


    Optional<Product> findFirstByNameAndDeletedFalse(String name);


    List<Product> findAllByBrandAndDeletedFalseAndSkuIdNot(String brand,String skuId);

    List<Product> findAllByBusinessSubcategoryIdAndDeletedFalseAndSkuIdNot(Long subCategoryId,String skuId);

    Boolean existsProductByNameIgnoreCaseAndSkuIdNot(String name, String skuId);



}
