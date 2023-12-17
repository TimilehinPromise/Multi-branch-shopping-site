package com.valuemart.shop.persistence.entity;

import com.valuemart.shop.domain.BranchModel;
import lombok.*;

import javax.persistence.*;


@Entity
@Builder
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Table(name = "branch")
public class Branch extends BasePersistentEntity implements ToModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(nullable = false)
    private String location;

    @Override
    public BranchModel toModel() {
        return BranchModel.builder()
                .name(name)
                .location(location)
                .build();
    }
}
