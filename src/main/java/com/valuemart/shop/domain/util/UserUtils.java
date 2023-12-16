package com.valuemart.shop.domain.util;

import com.valuemart.shop.exception.NotFoundException;
import com.valuemart.shop.persistence.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.valuemart.shop.domain.models.RoleType.*;


public final class UserUtils {

    private UserUtils() {
    }

    public static User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new NotFoundException("user not found");
        }
        return ((User) authentication.getPrincipal());
    }



}
