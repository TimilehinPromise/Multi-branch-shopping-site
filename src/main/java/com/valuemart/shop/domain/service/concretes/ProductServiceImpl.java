package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.ProductImageModel;
import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.ProductModel;
import com.valuemart.shop.domain.models.dto.ProductDTO;
import com.valuemart.shop.domain.models.enums.RelatedBy;
import com.valuemart.shop.domain.service.abstracts.ProductsService;
import com.valuemart.shop.domain.util.ProductUtil;
import com.valuemart.shop.domain.util.ResponseMessageUtil;
import com.valuemart.shop.exception.BadRequestException;
import com.valuemart.shop.exception.NotFoundException;
import com.valuemart.shop.persistence.entity.*;
import com.valuemart.shop.persistence.repository.BranchRepository;
import com.valuemart.shop.persistence.repository.BusinessCategoryRepository;
import com.valuemart.shop.persistence.repository.BusinessSubCategoryRepository;
import com.valuemart.shop.persistence.repository.ProductRepository;
import com.valuemart.shop.persistence.specification.ProductSpecification;
import com.valuemart.shop.persistence.specification.SearchCriteria;
import com.valuemart.shop.persistence.specification.SearchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.valuemart.shop.persistence.specification.ProductSpecification.searchInBranchWithKeyword;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductsService {

    private final BranchRepository branchRepository;
    private final BusinessCategoryRepository businessCategoryRepository;
    private final BusinessSubCategoryRepository businessSubCategoryRepository;
    private final ProductRepository productRepository;

    @Override
    public ResponseMessage createProduct(ProductDTO dto) {
        Product product = Product.fromModel(dto);

        if (productRepository.existsProductByNameIgnoreCaseAndDeletedFalse(dto.getName())){
            throw new BadRequestException("Product "+ dto.getName() + " already exists");
        }

        Map<Long, Branch> branches = getBranchMap();
        addBranchesToProduct(product, dto, branches);

        associateCategoryAndSubcategory(product, dto);
        addProductImages(product, dto.getImages());

        String tempSkuId = ProductUtil.generateTempSkuId(product);
        product.setSkuId(tempSkuId);

        Product savedProduct = productRepository.save(product);

        String finalSkuId  = ProductUtil.generateSkuId(savedProduct);
        savedProduct.setSkuId(finalSkuId);

        productRepository.save(savedProduct);
        return ResponseMessageUtil.createSuccessResponse("Product " + product.getName(), "created");
    }

    @Override
    public Page<ProductModel> getAllProduct(Pageable pageable){

        return productRepository.findAll(pageable).map(Product::toModel);
    }

    @Override
    public Page<ProductModel> getAllProductByCategory(Long id,Pageable pageable){

        return productRepository.findAllByBusinessCategoryIdAndDeletedFalse(id,pageable).map(Product::toModel);
    }

    @Override
    public Page<ProductModel>  getAllProductBySubCategory(Long id,Pageable pageable){

        return productRepository.findAllByBusinessSubcategoryIdAndDeletedFalse(id,pageable).map(Product::toModel);
    }

    @Override
    public ProductModel getProductBySkuId(String skuId){

        return productRepository.findFirstBySkuIdAndDeletedFalse(skuId).map(Product::toModel)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));
    }

    @Override
    public List<ProductModel> getProductRelatedBy(String related, String keyword,String productSku) {

        productRepository.findFirstBySkuIdAndDeletedFalse(productSku).map(Product::toModel)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        List<Product> products;

        if (related.equalsIgnoreCase(RelatedBy.BRAND.getRelatedByName())) {
            products = productRepository.findAllByBrandAndDeletedFalseAndSkuIdNot(keyword.toLowerCase(),productSku);
        } else if (related.equalsIgnoreCase(RelatedBy.SUBCATEGORY.getRelatedByName())) {
            products = productRepository.findAllByBusinessSubcategoryIdAndDeletedFalseAndSkuIdNot(Long.valueOf(keyword),productSku);
        } else {
            return Collections.emptyList();
        }

        return products.stream().map(Product::toModel).collect(Collectors.toList());
    }

    @Override
    public Page<ProductModel> filterProducts(String skuId,
                                             Long productName,
                                             String branch,
                                             Pageable pageable){

        ProductSpecification productSpecification =   buildSpecification(skuId,productName,branch);

        return productRepository.findAll(Specification.where(productSpecification), pageable).map(Product::toModel);

    }

    @Override
    public Page<ProductModel> searchProducts(String keyword,
                                             Long branchId,
                                             Pageable pageable){

        Map<Long, Branch> branches = getBranchMap();
        Set<Long>  branch = addBranchById(branchId, branches);

        Specification<Product> spec =  searchInBranchWithKeyword(branch,keyword);

        return productRepository.findAll(Specification.where(spec), pageable).map(Product::toModel);

    }


    private void associateCategoryAndSubcategory(Product product, ProductDTO dto) {
        BusinessCategory businessCategory = businessCategoryRepository
                .findFirstById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Business Category '" + dto.getCategoryId() + "' not found"));

        BusinessSubcategory businessSubcategory = businessSubCategoryRepository
                .findFirstById(dto.getSubCategoryId())
                .orElseThrow(() -> new NotFoundException("Business Subcategory '" + dto.getSubCategoryId() + "' not found"));

        product.setBusinessCategory(businessCategory);
        product.setBusinessSubcategory(businessSubcategory);
    }

    private void addProductImages(Product product, List<ProductImageModel> images) {
        for (ProductImageModel imageModel : images) {
            ProductImage image = new ProductImage(imageModel.getImageUrl(),product);
            product.addImage(image);
        }
    }

    private Map<Long, Branch> getBranchMap() {
        List<Branch> allBranches = branchRepository.findAll();
        return allBranches.stream().collect(Collectors.toMap(Branch::getId, Function.identity()));
    }

    private Set<Long> addBranchById(Long branchId, Map<Long, Branch> branches) {
        Set<Long> branchIdsSet = new HashSet<>();

        if (branchId != null && branches.containsKey(branchId) && (branchId == 1L || branchId == 2L || branchId == 3L)) {
            branchIdsSet.add(branchId);
        }

        return branchIdsSet;
    }





    private void addBranchesToProduct(Product product, ProductDTO dto, Map<Long, Branch> branches) {
        if (product.getBranches() == null) {
            product.setBranches(new HashSet<>());
        }
        if (dto.isAvailableInBranch1()) {
            product.getBranches().add(branches.get(1L));
        }
        if (dto.isAvailableInBranch2()) {
            product.getBranches().add(branches.get(2L));
        }
        if (dto.isAvailableInBranch3()) {
            product.getBranches().add(branches.get(3L));
        }
    }

    private ProductSpecification buildSpecification(String skuId,
                                                    Long productName,
                                                    String branch) {
        ProductSpecification specification = new ProductSpecification();
        if (productName != null) {
            specification.add(new SearchCriteria<>("name", productName, SearchOperation.IN));
        }

        if (skuId != null) {
            specification.add(new SearchCriteria<>("skuId", skuId, SearchOperation.MATCH));
        }

        if (branch != null) {
            specification.add(new SearchCriteria<>("branch", branch, SearchOperation.MATCH));
        }

        return specification;
    }

}
