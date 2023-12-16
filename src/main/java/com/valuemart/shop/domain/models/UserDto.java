package com.valuemart.shop.domain.models;

import com.valuemart.shop.persistence.entity.Authority;
import com.valuemart.shop.persistence.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

import static com.valuemart.shop.domain.util.FunctionUtil.emptyIfNullStream;
import static java.util.stream.Collectors.toSet;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstName;
    private String lastName;
    private String phone;
    private String roleType;



    public static UserDto valueOf(User user) {
        return new UserDto(

                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole().getName()

        );
    }

    static Set<String> extractAuthorities(User user) {
        return emptyIfNullStream(user.getUserAuthorities())
                .map(Authority::getAuthority)
                .collect(toSet());
    }
}
