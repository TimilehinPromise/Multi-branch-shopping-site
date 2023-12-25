package com.valuemart.shop.persistence.entity;

import com.valuemart.shop.domain.models.LoyaltyModel;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Table(name = "loyalty")
@Accessors(chain = true)
public class Loyalty extends BasePersistentEntity implements ToModel{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal requiredAmount;

    private int coinNo;

    private BigDecimal discountValue;

    @Override
    public LoyaltyModel toModel() {
        return LoyaltyModel.builder()
                .coinNo(this.coinNo)
                .requiredAmount(this.requiredAmount)
                .discountValue(this.discountValue)
                .build();
    }
}
