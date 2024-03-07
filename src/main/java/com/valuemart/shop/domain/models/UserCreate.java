package com.valuemart.shop.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreate {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String password;
    private String branchId;
    private String phone;
    @NotEmpty
    @Email
    private String email;
}
