package com.valuemart.shop.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valuemart.shop.persistence.entity.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserUpdate {


    private String phone;
    private List<AddressDTO> addressList;
    @JsonIgnore
    private User user;

}
