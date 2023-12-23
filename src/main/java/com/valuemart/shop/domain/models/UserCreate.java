package com.valuemart.shop.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreate {
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    private String password;
    private String branchId;
    @NotEmpty
    @Email
    private String email;
}
