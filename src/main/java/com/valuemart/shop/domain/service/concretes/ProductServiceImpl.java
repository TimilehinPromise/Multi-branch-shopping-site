package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.ProductImageModel;
import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.ProductModel;
import com.valuemart.shop.domain.models.Seasons;
import com.valuemart.shop.domain.models.dto.ProductDTO;
import com.valuemart.shop.domain.models.enums.RelatedBy;
import com.valuemart.shop.domain.service.abstracts.ProductsService;
import com.valuemart.shop.domain.util.ProductUtil;
import com.valuemart.shop.domain.util.ResponseMessageUtil;
import com.valuemart.shop.domain.util.UserUtils;
import com.valuemart.shop.exception.BadRequestException;
import com.valuemart.shop.exception.NotFoundException;
import com.valuemart.shop.persistence.entity.*;
import com.valuemart.shop.persistence.repository.*;
import com.valuemart.shop.persistence.specification.ProductSpecification;
import com.valuemart.shop.persistence.specification.SearchCriteria;
import com.valuemart.shop.persistence.specification.SearchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.valuemart.shop.domain.util.ProductUtil.getFormattedCellValue;
import static com.valuemart.shop.persistence.specification.ProductSpecification.searchInBranchWithKeyword;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductsService {

    private static final int MAX_IMAGE_COLUMNS = 4;
    private final BranchRepository branchRepository;
    private final BusinessCategoryRepository businessCategoryRepository;
    private final BusinessSubCategoryRepository businessSubCategoryRepository;
    private final ProductRepository productRepository;
    private final RecentlyViewedRepository recentlyViewedRepository;

    private static final String TYPE = "text/csv";

    @Override
    public ResponseMessage createProduct(ProductDTO dto) {
        Product product = Product.fromModel(dto);

        if (productRepository.existsProductByNameIgnoreCaseAndDeletedFalse(dto.getName())) {
            System.out.println("already exists");
            throw new BadRequestException("Product " + dto.getName() + " already exists");
        }

        Map<Long, Branch> branches = getBranchMap();
        addBranchesToProduct(product, dto, branches);

        associateCategoryAndSubcategory(product, dto);
        addProductImages(product, dto.getImages());

        String tempSkuId = ProductUtil.generateTempSkuId(product);
        product.setSkuId(tempSkuId);

        Product savedProduct = productRepository.save(product);

        String finalSkuId = ProductUtil.generateSkuId(savedProduct);
        savedProduct.setSkuId(finalSkuId);

        productRepository.save(savedProduct);
        return ResponseMessageUtil.createSuccessResponse("Product " + product.getName(), "created");
    }

    @Override
    public ResponseMessage updateProduct(ProductDTO dto) {
        Product product = Product.fromModel(dto);
        Product existingProduct = productRepository.findFirstBySkuIdAndDeletedFalse(dto.getSkuId()).get();

        if (productRepository.existsProductByNameIgnoreCaseAndSkuIdNot(dto.getName(), existingProduct.getSkuId())) {
            throw new BadRequestException("Product " + dto.getName() + " already exists");
        }

        Map<Long, Branch> branches = getBranchMap();
        editBranchesToProduct(existingProduct, dto, branches);

        associateCategoryAndSubcategory(product, dto);
        addProductImages(product, dto.getImages());
        updateExistingProductWithDTO(existingProduct, product);

        productRepository.save(existingProduct);
        return ResponseMessageUtil.createSuccessResponse("Product " + product.getName(), "updated");
    }

    @Override
    public ResponseMessage deleteProduct(String skuId){

        Product existingProduct = productRepository.findFirstBySkuIdAndDeletedFalse(skuId).get();

        existingProduct.setDeleted(true);

        productRepository.save(existingProduct);

        return ResponseMessageUtil.createSuccessResponse("Product " + existingProduct.getName(), " deleted");
    }

    private void updateExistingProductWithDTO(Product existingProduct, Product product) {
        if (!existingProduct.getName().equals(product.getName())) {
            existingProduct.setName(product.getName());
        }
        if (!existingProduct.getDescription().equals(product.getDescription())) {
            existingProduct.setDescription(product.getDescription());
        }
        if (existingProduct.getBrand() != null && !existingProduct.getBrand().equals(product.getBrand())) {
            existingProduct.setBrand(product.getBrand());
        }
        if (existingProduct.getPrice() != null && !existingProduct.getPrice().equals(product.getPrice())) {
            existingProduct.setPrice(product.getPrice());
        }
        if (existingProduct.isEnabled() != product.isEnabled()) {
            existingProduct.setEnabled(product.isEnabled());
        }
        if (existingProduct.getSeason().equals(product.getSeason())) {
            existingProduct.setSeason(product.getSeason());
        }
        if (existingProduct.getBusinessCategory().getId() != null && !existingProduct.getBusinessCategory().getId().equals(product.getBusinessCategory().getId())) {
            existingProduct.setBusinessCategory(product.getBusinessCategory());
        }
        if (existingProduct.getBusinessSubcategory().getId() != null && !existingProduct.getBusinessSubcategory().getId().equals(product.getBusinessSubcategory().getId())) {
            existingProduct.setBusinessSubcategory(product.getBusinessSubcategory());
        }
        if (existingProduct.getBusinessSubcategory().getId() != null && !existingProduct.getBusinessSubcategory().getId().equals(product.getBusinessSubcategory().getId())) {
            existingProduct.setBusinessSubcategory(product.getBusinessSubcategory());
        }
        updateProductImages(existingProduct, product);


    }

    private void updateProductImages(Product existingProduct, Product newProduct) {
        Set<String> existingImageUrls = existingProduct.getImages().stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toSet());

        Set<String> newImageUrls = newProduct.getImages().stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toSet());

        // Remove images that are no longer needed
        existingProduct.getImages().removeIf(image -> !newImageUrls.contains(image.getImageUrl()));

        // Add new images
        for (ProductImage newImage : newProduct.getImages()) {
            if (!existingImageUrls.contains(newImage.getImageUrl())) {
                existingProduct.addImage(new ProductImage(newImage.getImageUrl(), existingProduct));
            }
        }
    }


    @Override
    public List<ProductModel> getAllProduct() {
        return productRepository.findAllByDeletedFalse().stream()
                .sorted(Comparator.comparing(Product::getCreatedAt).reversed()).map(Product::toModel).toList();
    }



    @Override
    public long getAllProducts(){
        return productRepository.countAllByEnabledTrue();
    }

    @Override
    public List<ProductModel> getAllProductStore(Long branchId) {
        return productRepository.findAllProductByBranch(branchId).stream().map(Product::toModel).toList();
    }

    @Override
    public List<ProductModel> getAllProductByCategory(Long id) {

        return productRepository.findAllByBusinessCategoryIdAndDeletedFalse(id).stream().map(Product::toModel).toList();
    }

    @Override
    public List<ProductModel> getAllProductBySubCategory(Long id) {

        return productRepository.findAllByBusinessSubcategoryIdAndDeletedFalse(id).stream().map(Product::toModel).toList();
    }

    @Override
    public ProductModel getProductBySkuId(String skuId) {
        Optional<User> user = UserUtils.getLoggedInUserOptional();

        ProductModel model = productRepository.findFirstBySkuIdAndDeletedFalse(skuId).map(Product::toModel)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        if (user.isPresent()) {
            updateRecentlyViewed(user.get().getId(), model.getId());
        }

        return model;
    }

    public ProductModel getProductById(Long id) {
        ProductModel model = productRepository.findById(id).map(Product::toModel)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));
        return model;
    }

    @Async
    public void updateRecentlyViewed(Long userId, Long productId) {
        // Logic to handle recently viewed items
        RecentlyViewed recentlyViewed = recentlyViewedRepository.findByUserIdAndProductId(userId, productId);
        if (recentlyViewed != null) {
            recentlyViewed.setViewTimestamp(LocalDateTime.now());
        } else {
            if (recentlyViewedRepository.countByUserId(userId) >= 9) {
                Pageable limit = PageRequest.of(0, 1);
                List<RecentlyViewed> oldestViewed = recentlyViewedRepository.findOldestByUserId(userId, limit);
                recentlyViewedRepository.delete(oldestViewed.get(0));
            }
            recentlyViewed = new RecentlyViewed(userId, productId, LocalDateTime.now());
        }
        recentlyViewedRepository.save(recentlyViewed);

    }


    @Override
    public List<ProductModel> getRecentlyViewed(User user) {

        List<RecentlyViewed> recentlyViewedList = recentlyViewedRepository.findByUserId(user.getId());
        List<ProductModel> productList = new ArrayList<>();

        for (RecentlyViewed viewed : recentlyViewedList) {
            ProductModel model = getProductById(viewed.getProductId());
            productList.add(model);
        }
        productList.remove(productList.size()-1);

        return productList;
    }

    @Override
    public List<BusinessCategory> getCategories() {
        return businessCategoryRepository.findAllByDeletedFalse();
    }

    @Override
    public List<ProductModel> getProductRelatedBy(String related, String keyword, String productSku) {

        productRepository.findFirstBySkuIdAndDeletedFalse(productSku).map(Product::toModel)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        List<Product> products;

        if (related.equalsIgnoreCase(RelatedBy.BRAND.getRelatedByName())) {
            System.out.println("search by brand");
            products = productRepository.findAllByBrandIgnoreCaseAndDeletedFalseAndSkuIdNot(keyword.toLowerCase(), productSku);
        } else if (related.equalsIgnoreCase(RelatedBy.SUBCATEGORY.getRelatedByName())) {
            System.out.println("search by subcategory");
            products = productRepository.findAllByBusinessSubcategoryIdAndDeletedFalseAndSkuIdNot(Long.valueOf(keyword), productSku);
        } else {
            System.out.println("no valid search criteria");
            return Collections.emptyList();
        }

        return products.stream().map(Product::toModel).collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> getProductRelatedBy(String brand, String productSku,Long branchId) {

        Optional<Product> initialProductOpt = productRepository.findFirstBySkuIdAndDeletedFalse(productSku);
        Product initialProduct = initialProductOpt
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        Set<Product> productsSet = new LinkedHashSet<>();

        // Add products by brand
        System.out.println("search by brand");
        List<Product> brandProducts = productRepository.findAllByBrandIgnoreCaseAndDeletedFalseAndSkuIdNot(brand.toLowerCase(), productSku);
        productsSet.addAll(brandProducts);

        // If the initial product has a subcategory, add products from the same subcategory
        if (initialProduct.getBusinessSubcategory() != null) {
            Long subcategoryId = initialProduct.getBusinessSubcategory().getId();
            List<Product> subcategoryProducts = productRepository.findAllByBusinessSubcategoryIdAndDeletedFalseAndSkuIdNot(subcategoryId, productSku);
            productsSet.addAll(subcategoryProducts);
        }

        // Filter products by the branchId. Only include products available in the specified branch.
        return productsSet.stream()
                .filter(product -> product.getBranches().stream().anyMatch(branch -> branch.getId().equals(branchId)))
                .map(Product::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> getProductsBySeason() {
        Seasons currentSeason = Seasons.getCurrentSeason();
        return productRepository.findAllBySeasonAndDeletedFalse(currentSeason.name()).stream().map(Product::toModel).collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> getProductsBySeasonStore(Long branchId) {
        Seasons currentSeason = Seasons.getCurrentSeason();
        return productRepository.findAllBySeasonAndDeletedFalseStore(currentSeason.name(), branchId).stream().map(Product::toModel).collect(Collectors.toList());
    }


    @Override
    public Page<ProductModel> filterProducts(String skuId,
                                             Long productName,
                                             String branch,
                                             Pageable pageable) {

        ProductSpecification productSpecification = buildSpecification(skuId, productName, branch);

        return productRepository.findAll(Specification.where(productSpecification), pageable).map(Product::toModel);

    }

    @Override
    public Page<ProductModel> searchProducts(String keyword,
                                             Long branchId,
                                             Pageable pageable) {

        Map<Long, Branch> branches = getBranchMap();
        Set<Long> branch = addBranchById(branchId, branches);

        Specification<Product> spec = searchInBranchWithKeyword(branch, keyword);

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
            ProductImage image = new ProductImage(imageModel.getImageUrl(), product);
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

    private void editBranchesToProduct(Product product, ProductDTO dto, Map<Long, Branch> branches) {
        // Ensure the branches set is initialized
        if (product.getBranches() == null) {
            product.setBranches(new HashSet<>());
        }

        // Handle adding or removing branch 1
        updateBranchAvailability(product, branches.get(1L), dto.isAvailableInBranch1());

        // Handle adding or removing branch 2
        updateBranchAvailability(product, branches.get(2L), dto.isAvailableInBranch2());

        // Handle adding or removing branch 3
        updateBranchAvailability(product, branches.get(3L), dto.isAvailableInBranch3());
    }

    private void updateBranchAvailability(Product product, Branch branch, boolean isAvailable) {
        if (isAvailable) {
            // Add the branch if it's not already associated with the product
            product.getBranches().add(branch);
        } else {
            // Remove the branch if it's currently associated with the product
            product.getBranches().remove(branch);
        }
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

    @Override
    public List<String> processExcelFileToProducts(MultipartFile file) {
        List<String> errorLog = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {  // Use XSSFWorkbook for .xlsx files

            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
            if (sheet.getPhysicalNumberOfRows() == 0) {
                throw new ValidationException("Excel file has no data");
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Cell firstCell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String productName = firstCell.getStringCellValue();

                if (productName == null || productName.trim().isEmpty()) {
                    // Skip rows with an empty first cell (productName)
                    continue;
                }

                Optional<Product> existingProduct = productRepository.findFirstByNameAndDeletedFalse(productName);
                if (existingProduct.isPresent()) {
                    errorLog.add("Product already exists: " + productName);
                    continue;
                }

                ProductDTO productDTO = buildProductDTOFromRow(row, errorLog);
                if (productDTO != null) {

                    createProduct(productDTO);
                    log.info("Product processed: " + productName);
                } else {
                    log.info("Product processing failed for row: " + row.getRowNum());
                }
            }
        } catch (Exception e) {
            errorLog.add("Error processing file: " + e.getMessage());
        }
        return errorLog;
    }

//    public List<String> processExcelFileToDeviceGroup(MultipartFile file) {
//        List<String> errorLog = new ArrayList<>();
//
//        try (InputStream inputStream = file.getInputStream();
//             Workbook workbook = new XSSFWorkbook(inputStream)) {  // Use XSSFWorkbook for .xlsx files
//
//            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
//            if (sheet.getPhysicalNumberOfRows() == 0) {
//                throw new ValidationException("Excel file has no data");
//            }
//
//            for (Row row : sheet) {
//                if (row.getRowNum() == 0) continue; // Skip header row
//
//                String productName = row.getCell(0).getStringCellValue();
//                Optional<Product> existingProduct = productRepository.findFirstByNameAndDeletedFalse(productName);
//
//                if (existingProduct.isPresent()) {
//                    errorLog.add("Product already exists: " + productName);
//                    continue;
//                }
//
//                ProductDTO productDTO = buildProductDTOFromRow(row, errorLog);
//                log.info("done");
////                if (productDTO != null) {
////                    createProduct(productDTO);
////                }
//            }
//        } catch (Exception e) {
//            errorLog.add("Error processing file: " + e.getMessage());
//        }
//        return errorLog;
//    }

//    private ProductDTO buildProductDTOFromRow(Row row, List<String> errorLog) {
//        try {
//            ProductDTO productDTO = new ProductDTO();
//            productDTO.setName(row.getCell(0).getStringCellValue());
//            productDTO.setBrand(row.getCell(1).getStringCellValue());
//            productDTO.setDescription(row.getCell(2).getStringCellValue());
//            productDTO.setPrice(BigDecimal.valueOf(row.getCell(3).getNumericCellValue()));
//            productDTO.setEnabled(row.getCell(4).getBooleanCellValue());
//            productDTO.setAvailableInBranch1(row.getCell(5).getBooleanCellValue());
//            productDTO.setAvailableInBranch2(row.getCell(6).getBooleanCellValue());
//            productDTO.setAvailableInBranch3(row.getCell(7).getBooleanCellValue());
//
//            String categoryName = row.getCell(8).getStringCellValue();
//            String subCategoryName = row.getCell(9).getStringCellValue();
//            Optional<BusinessCategory> categoryOpt = businessCategoryRepository.findByName(categoryName);
//            Optional<BusinessSubcategory> subCategoryOpt = businessSubCategoryRepository.findByName(subCategoryName);
//            if (!categoryOpt.isPresent() || !subCategoryOpt.isPresent()) {
//                errorLog.add("Invalid category or subcategory in row " + row.getRowNum());
//                return null;
//            }
//            productDTO.setCategoryId(categoryOpt.get().getId());
//            productDTO.setSubCategoryId(subCategoryOpt.get().getId());
//
//            List<ProductImageModel> images = new ArrayList<>();
//            for (int i = 10; i <= 10 + MAX_IMAGE_COLUMNS - 1; i++) {
//                Cell imageCell = row.getCell(i);
//                if (imageCell != null) {
//                    String imageUrl = imageCell.getStringCellValue();
//                    if (!imageUrl.trim().isEmpty()) {
//                        images.add(new ProductImageModel(imageUrl.trim()));
//                    }
//                }
//            }
//            productDTO.setImages(images);
//            log.info(productDTO.toString());
//            return productDTO;
//        } catch (Exception e) {
//            errorLog.add("Invalid data in row " + row.getRowNum() + ": " + e.getMessage());
//            return null;
//        }
//    }

    private ProductDTO buildProductDTOFromRow(Row row, List<String> errorLog) {
        DataFormatter formatter = new DataFormatter(); // Create a formatter to handle different cell types.
        try {
            ProductDTO productDTO = new ProductDTO();

            // Using DataFormatter to avoid NullPointerException for String values
            productDTO.setName(formatter.formatCellValue(row.getCell(0)));
            productDTO.setBrand(formatter.formatCellValue(row.getCell(1)));
            productDTO.setDescription(formatter.formatCellValue(row.getCell(2)));

            // For numeric cells, still need to check if the cell is null
            Cell priceCell = row.getCell(3);
            if (priceCell != null && priceCell.getCellType() == CellType.NUMERIC) {
                productDTO.setPrice(BigDecimal.valueOf(priceCell.getNumericCellValue()));
            } else {
                errorLog.add("Price is invalid in row " + row.getRowNum());
                return null; // Or handle it differently
            }

            productDTO.setEnabled(getBooleanValue(row.getCell(4)));
            productDTO.setAvailableInBranch1(getBooleanValue(row.getCell(5)));
            productDTO.setAvailableInBranch2(getBooleanValue(row.getCell(6)));
            productDTO.setAvailableInBranch3(getBooleanValue(row.getCell(7)));

            String categoryName = formatter.formatCellValue(row.getCell(8));
            System.out.println(categoryName);
            String subCategoryName = formatter.formatCellValue(row.getCell(9));
            System.out.println(subCategoryName);

            categoryName = getFormattedCellValue(categoryName);
            subCategoryName = getFormattedCellValue(subCategoryName);
            Optional<BusinessCategory> categoryOpt = businessCategoryRepository.findByName(categoryName);
            Optional<BusinessSubcategory> subCategoryOpt = businessSubCategoryRepository.findByName(subCategoryName);
            if (!categoryOpt.isPresent() || !subCategoryOpt.isPresent()) {
                errorLog.add("Invalid category or subcategory in row " + row.getRowNum());
                return null;
            }
            productDTO.setCategoryId(categoryOpt.get().getId());
            productDTO.setSubCategoryId(subCategoryOpt.get().getId());

            // Handling images
            List<ProductImageModel> images = new ArrayList<>();
            for (int i = 10; i < 12; i++) {
                Cell imageCell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String imageUrl = formatter.formatCellValue(imageCell);
                if (!imageUrl.isEmpty()) {
                    images.add(new ProductImageModel(imageUrl));
                }
            }
            productDTO.setImages(images);

            String cellValue = formatter.formatCellValue(row.getCell(13)).toUpperCase().trim();

            System.out.println(cellValue);

            if (!cellValue.isEmpty()) {
                try {
                    productDTO.setSeason(Seasons.valueOf(cellValue).name());
                } catch (IllegalArgumentException e) {
                    productDTO.setSeason(Seasons.NONE.name());
                }
            } else {
                productDTO.setSeason(Seasons.NONE.name());
            }
            log.info(productDTO.toString());
            return productDTO;
        } catch (Exception e) {
            errorLog.add("Invalid data in row " + row.getRowNum() + ": " + e.getMessage());
            return null;
        }
    }

    private boolean getBooleanValue(Cell cell) {
        return cell != null && cell.getCellType() == CellType.BOOLEAN && cell.getBooleanCellValue();
    }


}
