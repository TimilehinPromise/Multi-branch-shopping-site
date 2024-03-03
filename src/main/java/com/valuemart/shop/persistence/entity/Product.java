package com.valuemart.shop.persistence.entity;

import com.valuemart.shop.domain.ProductImageModel;
import com.valuemart.shop.domain.models.ProductModel;
import com.valuemart.shop.domain.models.Seasons;
import com.valuemart.shop.domain.models.dto.ProductDTO;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Builder
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Table(name = "product")
@Accessors(chain = true)
public class Product extends BasePersistentEntity implements ToModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(nullable = false)
    private String description;
    private String brand;
    @Column(nullable = false,name = "price")
    private BigDecimal price;
    @Column(nullable = false)
    private boolean deleted;
    @Column(nullable = false)
    private boolean enabled;
    @Column(nullable = false)
    private String skuId;
    private String season;
    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private BusinessSubcategory businessSubcategory;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private BusinessCategory businessCategory;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_branch",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "branch_id")
    )
    private Set<Branch> branches = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images  = new ArrayList<>();

    public static class ProductBuilder {
        private Set<Branch> branches = new HashSet<>();
        private List<ProductImage> images = new ArrayList<>();

        // Custom builder methods
    }


    public ProductModel toModel() {
        List<ProductImageModel> imageModels = new ArrayList<>();
        if (images != null) {
            for (ProductImage image : images) {
                imageModels.add(new ProductImageModel(image.getImageUrl()));
            }
        }

        return ProductModel.builder()
                .name(this.name)
                .skuId(this.skuId)
                .brand(this.brand)
                .description(this.description)
                .categoryName(this.businessCategory != null ? this.businessCategory.getName() : null)
                .subcategoryName(this.businessSubcategory != null ? this.businessSubcategory.getName() : null)
                .price(this.price)
                .createdAt(this.createdAt)
                .season(this.season)
                .enabled(this.enabled)
                .id(this.id)
                .images(imageModels) // Adding the list of image models
                .build();
    }


    public void addImage(ProductImage image) {
        images.add(image);
        image.setProduct(this);
    }

    // Method to remove an image
    public void removeImage(ProductImage image) {
        images.remove(image);
        image.setProduct(null);
    }

    public static Product fromModel(ProductDTO dto) {
        return Product.builder()
                .name(dto.getName())
                .brand(dto.getBrand())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .enabled(dto.isEnabled())
                .deleted(false)
                .season(fromString(dto.getSeason()))
                .build();
    }

    public static String fromString(String seasonStr) {
        if (seasonStr == null || seasonStr.isEmpty()) {
            return Seasons.NONE.name(); //
        }
        try {
            return Seasons.valueOf(seasonStr.toUpperCase()).name();
        } catch (IllegalArgumentException e) {
            return null; // or handle invalid values appropriately
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
