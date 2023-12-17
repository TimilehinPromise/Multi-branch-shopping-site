package com.valuemart.shop.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    public ProductImage(String imageUrl, Product product) {
        this.imageUrl = imageUrl;
        this.product = product;
    }

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

}
