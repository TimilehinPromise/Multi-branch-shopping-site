package com.valuemart.shop.persistence.entity;


import com.valuemart.shop.domain.models.AddressModel;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "address")
public class Address extends BasePersistentEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String street;

    private boolean preferred;

    private String city;

    private String landmark;

    public AddressModel toModel() {
        return AddressModel.builder()
                .addressId(id)
                .street(street)
                .city(city)
                .landmark(landmark)
                .preferred(preferred)
                .build();
    }

}
