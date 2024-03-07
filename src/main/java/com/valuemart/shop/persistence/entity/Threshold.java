package com.valuemart.shop.persistence.entity;

import com.valuemart.shop.domain.models.dto.ThresholdDTO;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@ToString
@Table(name = "threshold")
public class Threshold extends BasePersistentEntity implements ToModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal value;

    private BigDecimal monetaryAmount;

    @Override
    public ThresholdDTO toModel() {
        ThresholdDTO dto = new ThresholdDTO();
        dto.setId(this.id);
        dto.setValue(this.value);
        dto.setMonetaryAmount(this.monetaryAmount);
        return dto;
    }

}
