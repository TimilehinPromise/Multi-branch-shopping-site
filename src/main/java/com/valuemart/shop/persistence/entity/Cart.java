package com.valuemart.shop.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="cart")
@Builder
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class Cart extends BasePersistentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @JsonIgnore
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "price")
    private BigDecimal price;


    private int quantity;



//    public Cart() {
//    }

//    public Cart(Product product, int quantity, User user, BigDecimal price){
//        this.user = user;
//        this.product = product;
//        this.quantity = quantity;
//        this.price = price;
//    }


}