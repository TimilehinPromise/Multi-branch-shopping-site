package com.valuemart.shop.domain.models;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Data
@Builder
public class LoginResponseModel {
     String token;
     LocalDateTime expires;
     String firstName;
     String lastName;
     String email;
     String type;
}
