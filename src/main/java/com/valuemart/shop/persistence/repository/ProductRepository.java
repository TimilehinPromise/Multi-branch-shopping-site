package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.BusinessCategory;
import com.valuemart.shop.persistence.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {


    boolean existsProductByNameIgnoreCaseAndDeletedFalse(String name);

    List<Product> findAllByBusinessCategoryIdAndDeletedFalse(Long categoryId);

//    @Query("select p from Product p where p.deleted = false and p.branches = :branchId")
    @Query("SELECT p FROM Product p JOIN p.branches b WHERE p.deleted = false AND b.id = :branchId ORDER BY p.createdAt DESC")
    List<Product>findAllProductByBranch(Long branchId);
    List<Product>findAllByDeletedFalse();


    List<Product>  findAllByBusinessSubcategoryIdAndDeletedFalse(Long subCategoryId);


    Optional<Product>findFirstBySkuIdAndDeletedFalse(String skuId);

    Optional<Product> findFirstBySkuIdAndDeletedFalseAndBranches_Id(String skuId, Long branchId);


    Optional<Product> findFirstByNameAndDeletedFalse(String name);


    List<Product> findAllByBrandIgnoreCaseAndDeletedFalseAndSkuIdNot(String brand,String skuId);

    List<Product> findAllBySeasonAndDeletedFalse(String season);

    @Query("select p from Product  p JOIN p.branches b where p.season = :season and p.deleted = false and b.id = :branchId ")
    List<Product> findAllBySeasonAndDeletedFalseStore(String season,Long branchId);

    List<Product> findAllByBusinessSubcategoryIdAndDeletedFalseAndSkuIdNot(Long subCategoryId,String skuId);

    Boolean existsProductByNameIgnoreCaseAndSkuIdNot(String name, String skuId);


    long countAllByEnabledTrue();



}
