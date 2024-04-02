package com.valuemart.shop.persistence.entity;

import com.valuemart.shop.domain.models.DeliveryModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery_areas")
public class DeliveryArea implements ToModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "area_name", unique = true, nullable = false)
    private String areaName;

    @Column(name = "delivery_price", nullable = false)
    private BigDecimal deliveryPrice;

    @Override
    public DeliveryModel toModel() {
        return DeliveryModel.builder()
                .deliveryPrice(deliveryPrice)
                .areaName(areaName)
                .build();
    }
}
